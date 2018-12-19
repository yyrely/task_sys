package com.chuncongcong.task.service.impl;

import com.chuncongcong.task.context.RequestContext;
import com.chuncongcong.task.exception.BaseErrorCode;
import com.chuncongcong.task.exception.ServiceException;
import com.chuncongcong.task.manager.DepartmentManager;
import com.chuncongcong.task.manager.MessageBoardManager;
import com.chuncongcong.task.manager.TaskManager;
import com.chuncongcong.task.manager.UsersManager;
import com.chuncongcong.task.model.constants.*;
import com.chuncongcong.task.model.domain.MessageCondition;
import com.chuncongcong.task.model.domain.TaskConditionBean;
import com.chuncongcong.task.model.dto.TipDTO;
import com.chuncongcong.task.model.dto.UserDTO;
import com.chuncongcong.task.model.entity.Department;
import com.chuncongcong.task.model.entity.Users;
import com.chuncongcong.task.model.vo.*;
import com.chuncongcong.task.service.UserService;
import com.chuncongcong.task.utils.AESUtil;
import com.chuncongcong.task.utils.JsonUtils;
import com.chuncongcong.task.utils.MessageBoardUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Value("${token.timeout}")
    private Integer tokenTimeout;

    private final StringRedisTemplate stringRedisTemplate;
    private final UsersManager usersManager;
    private final TaskManager taskManager;
    private final MessageBoardManager messageBoardManager;
    private final DepartmentManager departmentManager;

    public UserServiceImpl(StringRedisTemplate stringRedisTemplate, UsersManager usersManager, TaskManager taskManager, MessageBoardManager messageBoardManager, DepartmentManager departmentManager) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.usersManager = usersManager;
        this.taskManager = taskManager;
        this.messageBoardManager = messageBoardManager;
        this.departmentManager = departmentManager;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public Object checkUsername(String username) {

        if(StringUtils.isEmpty(username)){
            throw new ServiceException(BaseErrorCode.PARAM_ILLEGAL);
        }
        Users users = usersManager.getUsersByUsername(username);
        if (users != null) {
            throw new ServiceException(BaseErrorCode.PARAM_ILLEGAL);
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object register(UserDTO userDTO) throws Exception {

        String decodePassword = decodePassword(userDTO.getPassword(), userDTO.getTimestamp());
        checkUsername(userDTO.getUsername());
        userDTO.setPassword(Md5Crypt.md5Crypt(decodePassword.trim().getBytes(), "$1$12345678"));

        Users user = new Users();
        BeanUtils.copyProperties(user, userDTO);
        user.setDepartmentId(4L);
        usersManager.addUser(user);
        user.setPassword(null);
        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(usersVO,user);

        return usersVO;
    }

    @Override
    public Object login(UserDTO userDTO) throws Exception {

        if (StringUtils.isBlank(userDTO.getUsername()) || StringUtils.isBlank(userDTO.getPassword())) {
            throw new ServiceException(BaseErrorCode.LOGIN_ILLEGAL);
        }
        String decodePassword = decodePassword(userDTO.getPassword(), userDTO.getTimestamp());
        Users existUser = usersManager.getUsersByUsername(userDTO.getUsername());
        if (existUser == null) {
            throw new ServiceException(BaseErrorCode.LOGIN_ILLEGAL);
        }

        if (!Md5Crypt.md5Crypt(decodePassword.trim().getBytes(), "$1$12345678").equals(existUser.getPassword())) {
            throw new ServiceException(BaseErrorCode.LOGIN_ILLEGAL);
        }


        Department department = departmentManager.getDepartment(existUser.getDepartmentId());
        existUser.setPassword(null);

        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(usersVO, existUser);
        usersVO.setDepartment(department);

        //防止重复登录
        String token = stringRedisTemplate.opsForValue().get(Contants.ONLINE_USER + userDTO.getUsername());
        if (!StringUtils.isBlank(token)) {
            stringRedisTemplate.expire(Contants.ONLINE_USER + userDTO.getUsername(), 0, TimeUnit.SECONDS);
            stringRedisTemplate.opsForValue().set(Contants.ONLINE_TOKEN + token, "other login");
            stringRedisTemplate.expire(Contants.ONLINE_TOKEN + token, 600, TimeUnit.SECONDS);
        }
        token = UUID.randomUUID().toString();
        stringRedisTemplate.opsForValue().set(Contants.ONLINE_TOKEN + token, JsonUtils.toJSON(existUser));
        stringRedisTemplate.expire(Contants.ONLINE_TOKEN + token, tokenTimeout, TimeUnit.SECONDS);
        stringRedisTemplate.opsForValue().set(Contants.ONLINE_USER + existUser.getUsername(), token);
        stringRedisTemplate.expire(Contants.ONLINE_USER + existUser.getUsername(), tokenTimeout, TimeUnit.SECONDS);

        return TokenVO.builder()
                .token(token)
                .usersVO(usersVO)
                .build();
    }


    @Override
    public Object logout() {

        Users users = RequestContext.getUserInfo();
        String token = stringRedisTemplate.opsForValue().get(Contants.ONLINE_USER + users.getUsername());

        stringRedisTemplate.expire(Contants.ONLINE_USER + users.getUsername(), 0, TimeUnit.SECONDS);
        stringRedisTemplate.expire(Contants.ONLINE_TOKEN + token, 0, TimeUnit.SECONDS);

        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public Object getUser(Long userId) {
        Users user = RequestContext.getUserInfo();
        UsersVO usersVO = usersManager.getUsersById(userId);

        if (userId == user.getId()) {
            usersVO.setIsPraise(PraiseType.UNKNOWN);
        } else {
            Set<String> userIds = stringRedisTemplate.opsForSet().members(Contants.PRAISE_ID + userId);
            if(userIds != null && userIds.size() != 0){
                if (userIds.contains(String.valueOf(user.getId()))) {
                    usersVO.setIsPraise(PraiseType.ALREADY_PRAISE);
                } else {
                    usersVO.setIsPraise(PraiseType.NOT_PRAISE);
                }
            } else {
                usersVO.setIsPraise(PraiseType.NOT_PRAISE);
            }
        }
        return usersVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public Object getUserList(String username, Long[] departmentIds) {

        if (StringUtils.isEmpty(username)) {
            username = null;
        }

        if (departmentIds == null || departmentIds.length == 0) {
            departmentIds = null;
        }
        List<UsersVO> users = usersManager.getUserList(username, departmentIds);

        //判断是否在线
        Set<String> onlineUserKey = stringRedisTemplate.execute((RedisCallback<Set<String>>) (connection) -> {
                Set<String> keys = new HashSet<>();
                Cursor<byte[]> cursor = connection.scan(new ScanOptions.ScanOptionsBuilder().match(Contants.ONLINE_USER + "*").count(1000).build());
                while (cursor.hasNext()) {
                    keys.add(new String(cursor.next()));
                }
                return keys;
        });

        List<String> onlineUser = new ArrayList<>();
        if(onlineUserKey != null && onlineUserKey.size() != 0){
            for(String key : onlineUserKey){
                onlineUser.add(key.substring(Contants.ONLINE_USER.length()));
            }
        }

        for (UsersVO usersVO : users) {
            if (onlineUser.contains(usersVO.getUsername())) {
                usersVO.setIsOnline(OnlineType.ONLINE);
            }else{
                usersVO.setIsOnline(OnlineType.NOT_ONLINE);
            }
        }
        return users.stream().sorted((x,y) -> {
            if(x.getIsOnline() == y.getIsOnline()){
                return x.getRank() < y.getRank() ? 1 : -1;
            }else {
                return x.getIsOnline().ordinal() < y.getIsOnline().ordinal() ? 1 : -1;
            }
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object updateUser(Long userId, UserDTO userDTO) throws Exception {

        Users user = RequestContext.getUserInfo();

        UsersVO usersVO = usersManager.getUsersById(userId);

        if (usersVO == null || user.getId() != userId) {
            throw new ServiceException(BaseErrorCode.AUTHORITY_ILLEGAL);
        }

        Users updateUser = new Users();
        BeanUtils.copyProperties(updateUser, usersVO);
        updateUser.setDepartmentId(usersVO.getDepartment().getId());

        if (!StringUtils.isEmpty(userDTO.getUsername()) && !user.getUsername().equals(userDTO.getUsername())) {
            checkUsername(userDTO.getUsername());
            updateUser.setUsername(userDTO.getUsername());
        }

        if (!StringUtils.isEmpty(userDTO.getPassword())) {
            if (!userDTO.getPassword().equals(userDTO.getRePassword())) {
                throw new ServiceException(BaseErrorCode.PARAM_ILLEGAL);
            }
            String decodePassword = decodePassword(userDTO.getPassword(), userDTO.getTimestamp());
            updateUser.setPassword(Md5Crypt.md5Crypt(decodePassword.trim().getBytes(), "$1$12345678"));
        }

        if (!StringUtils.isEmpty(userDTO.getPortrait())) {
            updateUser.setPortrait(userDTO.getPortrait());
        }

        if (userDTO.getGender() != null) {
            updateUser.setGender(userDTO.getGender());
        }

        if (userDTO.getDepartmentId() != null) {
            updateUser.setDepartmentId(userDTO.getDepartmentId());
        }

        if (userDTO.getRole() != null) {
            updateUser.setRole(userDTO.getRole());
        }

        if (userDTO.getBirthday() != null) {
            updateUser.setBirthday(userDTO.getBirthday());
        }

        if (!StringUtils.isEmpty(userDTO.getMobilePhone())) {
            updateUser.setMobilePhone(userDTO.getMobilePhone());
        }

        if (!StringUtils.isEmpty(userDTO.getLandlinePhone())) {
            updateUser.setLandlinePhone(userDTO.getLandlinePhone());
        }

        if (!StringUtils.isEmpty(userDTO.getPosition())) {
            updateUser.setPosition(userDTO.getPosition());
        }

        usersManager.updateUsers(updateUser);

        updateUser.setPassword(null);
        //更新redis中用户的信息
        String token = stringRedisTemplate.opsForValue().get(Contants.ONLINE_USER + updateUser.getUsername());
        stringRedisTemplate.opsForValue().set(Contants.ONLINE_TOKEN + token, JsonUtils.toJSON(updateUser));

        return null;
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object updateUserPraise(Long userId) {

        Users user = RequestContext.getUserInfo();
        List<Long> userIds = Collections.singletonList(userId);
        Set<String> members = stringRedisTemplate.opsForSet().members(Contants.PRAISE_ID + userId);

        if(members != null && members.size()!=0){
            //点赞
            if(!members.contains(String.valueOf(user.getId()))){
                stringRedisTemplate.opsForSet().add(Contants.PRAISE_ID + userId,String.valueOf(user.getId()));
                usersManager.updateUserPraise(userId, 1);
                usersManager.updateUserRank(userIds, 2);
            } else {
                //取消点赞
                stringRedisTemplate.opsForSet().remove(Contants.PRAISE_ID + userId, String.valueOf(user.getId()));
                usersManager.updateUserPraise(userId, -1);
                usersManager.updateUserRank(userIds, -2);
            }
        }else{
            //自己不能给自己点赞
            if(userId == user.getId()){
                throw new ServiceException(BaseErrorCode.MAKE_ILLEGAL);
            }
            //第一次给人点赞
            stringRedisTemplate.opsForSet().add(Contants.PRAISE_ID + userId,String.valueOf(user.getId()));
            usersManager.updateUserPraise(userId, 1);
            usersManager.updateUserRank(userIds, 2);
        }
        return null;
    }

    @Async
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void loginSuccessAddRank(Long userId) {

        Set<String> loginUserIds = stringRedisTemplate.opsForSet().members(Contants.TODAY_LOGIN_USER_ID);
        List<Long> userIds = Collections.singletonList(userId);
        if(loginUserIds != null && loginUserIds.size() != 0){
            if(!loginUserIds.contains(String.valueOf(userId))){
                stringRedisTemplate.opsForSet().add(Contants.TODAY_LOGIN_USER_ID,String.valueOf(userId));
                usersManager.updateUserRank(userIds, 2);
            }
        }else{
            //第二天零点距现在的时间(秒)
            long expireTime = LocalDateTime.of(LocalDate.now().plus(1, ChronoUnit.DAYS), LocalTime.MIN).toInstant(ZoneOffset.of("+8")).getEpochSecond() - Instant.now().getEpochSecond();
            stringRedisTemplate.opsForSet().add(Contants.TODAY_LOGIN_USER_ID,String.valueOf(userId));
            stringRedisTemplate.expire(Contants.TODAY_LOGIN_USER_ID,expireTime,TimeUnit.SECONDS);
            usersManager.updateUserRank(userIds, 2);
        }
    }

    @Override
    public Object getTip() {

        Users user = RequestContext.getUserInfo();

        Set<String> taskTipUserIds = stringRedisTemplate.opsForSet().members(Contants.TASK_TIP_USER_ID);
        Set<String> messageTipUserIds = stringRedisTemplate.opsForSet().members(Contants.MESSAGE_TIP_USER_ID);

        TipVO tipVO = new TipVO();
        //返回每日任务提醒
        if((taskTipUserIds == null || taskTipUserIds.size() == 0) || !taskTipUserIds.contains(String.valueOf(user.getId()))){
            TaskConditionBean taskConditionBean = new TaskConditionBean();
            taskConditionBean.setUserIds(Collections.singletonList(user.getId()));
            List<TaskVO> taskList = taskManager.getTaskList(taskConditionBean);
            if(taskList != null && taskList.size() != 0){
                tipVO.setTaskList(taskList.stream()
                        .filter(x -> !x.getTaskCurrentState().equals(TaskCurrentState.ADVANCE_FINISH) && !x.getTaskCurrentState().equals(TaskCurrentState.FINISH))
                        .collect(Collectors.toList()));
            }else{
                tipVO.setTaskList(new ArrayList<>());
            }
        }

        //返回每日收信提醒
        if((messageTipUserIds == null || messageTipUserIds.size() == 0) || !messageTipUserIds.contains(String.valueOf(user.getId()))){
            MessageCondition messageCondition = new MessageCondition();
            messageCondition.setToUserId(user.getId());
            List<MessageBoardVO> messageList = messageBoardManager.getMessageList(messageCondition);
            if(messageList != null && messageList.size() != 0){
                tipVO.setMessageList(MessageBoardUtils.isReadComment(messageList, user.getUsername())
                        .stream().filter(x -> x.getIsRead().equals(ReadType.UNREAD))
                        .sorted((x, y) -> y.getUpdateTime().compareTo(x.getUpdateTime()))
                        .collect(Collectors.toList()));
            }else{
                tipVO.setMessageList(new ArrayList<>());
            }
        }

        return tipVO;
    }

    @Override
    public Object updateTip(TipDTO tipDTO) {
        Users user = RequestContext.getUserInfo();

        if (tipDTO.getTaskTip() != null && tipDTO.getTaskTip()) {
            Set<String> taskTipUserIds = stringRedisTemplate.opsForSet().members(Contants.TASK_TIP_USER_ID);
            if(taskTipUserIds == null || taskTipUserIds.size() == 0){
                long expireTime = LocalDateTime.of(LocalDate.now().plus(1, ChronoUnit.DAYS), LocalTime.MIN).toInstant(ZoneOffset.of("+8")).getEpochSecond() - Instant.now().getEpochSecond();
                stringRedisTemplate.opsForSet().add(Contants.TASK_TIP_USER_ID, String.valueOf(user.getId()));
                stringRedisTemplate.expire(Contants.TASK_TIP_USER_ID,expireTime,TimeUnit.SECONDS);
            }else{
                stringRedisTemplate.opsForSet().add(Contants.TASK_TIP_USER_ID, String.valueOf(user.getId()));
            }
        }

        if (tipDTO.getMessageTip() != null && tipDTO.getMessageTip()) {
            Set<String> messageTipUserIds = stringRedisTemplate.opsForSet().members(Contants.MESSAGE_TIP_USER_ID);
            if(messageTipUserIds == null || messageTipUserIds.size() == 0){
                long expireTime = LocalDateTime.of(LocalDate.now().plus(1, ChronoUnit.DAYS), LocalTime.MIN).toInstant(ZoneOffset.of("+8")).getEpochSecond() - Instant.now().getEpochSecond();
                stringRedisTemplate.opsForSet().add(Contants.MESSAGE_TIP_USER_ID, String.valueOf(user.getId()));
                stringRedisTemplate.expire(Contants.MESSAGE_TIP_USER_ID,expireTime,TimeUnit.SECONDS);
            }else{
                stringRedisTemplate.opsForSet().add(Contants.MESSAGE_TIP_USER_ID, String.valueOf(user.getId()));
            }
        }

        return null;
    }

    private String decodePassword(String password, String timestamp) throws Exception {
        String key = Contants.ASE_KEY_PRE + timestamp;
        //判断时间戳是否失效,大于两分钟的失效
        if (System.currentTimeMillis() / 1000 - Long.parseLong(timestamp) > 120) {
            throw new ServiceException(BaseErrorCode.LOGIN_ILLEGAL);
        }
        //解密
        password = URLDecoder.decode(password, "utf-8");
        password = AESUtil.aesDecrypt(password, key);

        return password;
    }


}
