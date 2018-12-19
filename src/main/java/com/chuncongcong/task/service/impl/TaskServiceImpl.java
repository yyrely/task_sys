package com.chuncongcong.task.service.impl;

import com.chuncongcong.task.context.RequestContext;
import com.chuncongcong.task.exception.BaseErrorCode;
import com.chuncongcong.task.exception.ServiceException;
import com.chuncongcong.task.handler.JsonTypeHandler;
import com.chuncongcong.task.manager.TaskManager;
import com.chuncongcong.task.manager.TaskUpdateManager;
import com.chuncongcong.task.manager.TaskUserManager;
import com.chuncongcong.task.manager.UsersManager;
import com.chuncongcong.task.model.constants.*;
import com.chuncongcong.task.model.domain.TaskConditionBean;
import com.chuncongcong.task.model.dto.TaskDTO;
import com.chuncongcong.task.model.entity.Task;
import com.chuncongcong.task.model.entity.TaskUpdate;
import com.chuncongcong.task.model.entity.TaskUser;
import com.chuncongcong.task.model.entity.Users;
import com.chuncongcong.task.model.vo.TaskListVO;
import com.chuncongcong.task.model.vo.TaskVO;
import com.chuncongcong.task.model.vo.UsersVO;
import com.chuncongcong.task.service.TaskService;
import com.chuncongcong.task.utils.JsonUtils;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.chuncongcong.task.model.constants.TaskCurrentState.NOT_START;

/**
 * @author Hu
 * @date 2018/8/7 11:00
 */

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskManager taskManager;
    private final TaskUserManager taskUserManager;
    private final TaskUpdateManager taskUpdateManager;
    private final UsersManager usersManager;
    private final StringRedisTemplate stringRedisTemplate;

    public TaskServiceImpl(TaskManager taskManager, TaskUserManager taskUserManager, TaskUpdateManager taskUpdateManager, UsersManager usersManager, StringRedisTemplate stringRedisTemplate) {
        this.taskManager = taskManager;
        this.taskUserManager = taskUserManager;
        this.taskUpdateManager = taskUpdateManager;
        this.usersManager = usersManager;
        this.stringRedisTemplate = stringRedisTemplate;
    }


    @Override
    public Object getAllTask(Long parentId) {
        Users user = RequestContext.getUserInfo();
        //查看所有任务权限
        if (parentId.equals(0L) && user.getRole() != RoleType.GENERAL_MANAGER) {
            throw new ServiceException(BaseErrorCode.AUTHORITY_ILLEGAL);
        }

        //查看子任务权限
        if (!parentId.equals(0L) && user.getRole().ordinal() < 2) {
            List<Long> taskUserIds = taskUserManager.getUserIds(parentId).stream().map(TaskUser::getUserId).collect(Collectors.toList());
            if (!taskUserIds.contains(user.getId())) {
                throw new ServiceException(BaseErrorCode.AUTHORITY_ILLEGAL);
            }
        }

        TaskConditionBean taskConditionBean = new TaskConditionBean();
        taskConditionBean.setParentId(parentId);

        return checkTaskCurrentState(taskManager.getTaskList(taskConditionBean));
    }

    @Override
    public Object getTaskByUsers(List<Long> userIds) {

        Users user = RequestContext.getUserInfo();

        if (user.getRole() == RoleType.CHARGE) {
            //该部门下成员
            List<Long> userIdList = usersManager.getUserListByDepartmentId(user.getDepartmentId())
                    .stream()
                    .map(Users::getId)
                    .collect(Collectors.toList());

            //有其他部门的成员,则权限不足
            if (!userIdList.containsAll(userIds)) {
                throw new ServiceException(BaseErrorCode.AUTHORITY_ILLEGAL);
            }
        }

        if (user.getRole() == RoleType.NORMAL) {
            //不可看别人的任务
            if (userIds.size() != 1 || !user.getId().equals(userIds.get(0))) {
                throw new ServiceException(BaseErrorCode.AUTHORITY_ILLEGAL);
            }
        }

        List<UsersVO> ret = taskManager.getTaskByUsers(userIds);
        ret.parallelStream().forEach(r -> r.setTaskVOList(convertTaskVOTo2D(r.getTaskVOList())));
        return ret;
    }

    @Override
    public Object getTaskByDepartments(List<Long> departmentIds) {

        Users user = RequestContext.getUserInfo();
        //主管权限
        if (user.getRole() == RoleType.CHARGE) {
            if (departmentIds.size() != 1) {
                throw new ServiceException(BaseErrorCode.AUTHORITY_ILLEGAL);
            } else {
                if (!user.getDepartmentId().equals(departmentIds.get(0))) {
                    throw new ServiceException(BaseErrorCode.AUTHORITY_ILLEGAL);
                }
            }
        }
        //普通员工权限
        if (user.getRole() == RoleType.NORMAL) {
            throw new ServiceException(BaseErrorCode.AUTHORITY_ILLEGAL);
        }
        //封装查询条件
        TaskConditionBean taskCondition = new TaskConditionBean();
        taskCondition.setDepartmentIds(departmentIds);
        taskCondition.setParentId(0L);

        return checkTaskCurrentState(taskManager.getTaskList(taskCondition));
    }

    @Override
    public Object getTaskByType(TaskType taskType) {

        Users user = RequestContext.getUserInfo();

        TaskConditionBean taskCondition = new TaskConditionBean();
        taskCondition.setTaskType(taskType);
        taskCondition.setParentId(0L);
        //主管看部门下
        if (user.getRole() == RoleType.CHARGE) {
            List<Users> userList = usersManager.getUserListByDepartmentId(user.getDepartmentId());
            taskCondition.setUserIds(userList.stream().map(Users::getId).collect(Collectors.toList()));
        }
        //个人看自己
        if (user.getRole() == RoleType.NORMAL) {
            taskCondition.setUserIds(Collections.singletonList(user.getId()));
        }

        return checkTaskCurrentState(taskManager.getTaskList(taskCondition));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object getTaskInfo(Long taskId, Long departmentId) {

        Users user = RequestContext.getUserInfo();
        if (!user.getDepartmentId().equals(departmentId) && user.getRole().ordinal() != 3) {
            throw new ServiceException(BaseErrorCode.AUTHORITY_ILLEGAL);
        }
        //获取任务详情
        TaskVO taskVO = taskManager.getTaskInfo(taskId);

        //加入任务更新记录
        List<TaskUpdate> taskUpdateList = PageHelper.startPage(1, 3).<TaskUpdate>doSelectPageInfo(
                () -> taskUpdateManager.getTaskUpdateList(taskId)).getList();
        taskVO.setTaskUpdates(taskUpdateList);

        //更新一下任务当前状态
        if (taskVO.getTaskCurrentState() == NOT_START) {
            if (taskVO.getStartTime().getEpochSecond() <= Instant.now().getEpochSecond()) {
                taskVO.setTaskCurrentState(TaskCurrentState.RUNNING);
                Task task = new Task();
                task.setId(taskId);
                task.setTaskCurrentState(taskVO.getTaskCurrentState());
                taskManager.updateTask(task);
            }
        }

        return taskVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object addTask(Long departmentId, TaskDTO taskDTO) throws Exception {

        Users user = RequestContext.getUserInfo();

        if (!user.getDepartmentId().equals(departmentId)) {
            throw new ServiceException(BaseErrorCode.AUTHORITY_ILLEGAL);
        }
        //判断参数
        if (StringUtils.isEmpty(taskDTO.getName()) || StringUtils.isEmpty(taskDTO.getCreateUser()) || taskDTO.getStartTime() == null || taskDTO.getEndTime() == null || taskDTO.getTotalTime() == null) {
            throw new ServiceException(BaseErrorCode.PARAM_ILLEGAL);
        }

        //添加任务
        Task task = new Task();
        BeanUtils.copyProperties(taskDTO, task);

        if (task.getStartTime().getEpochSecond() > Instant.now().getEpochSecond()) {
            task.setTaskCurrentState(NOT_START);
        } else {
            task.setTaskCurrentState(TaskCurrentState.RUNNING);
        }
        task.setTaskSubmitState(TaskSubmitState.UNKNOWN);
        task.setExtFile(JsonUtils.toJSON(taskDTO.getExtFile()));
        taskManager.addTask(task);
        taskUpdateManager.addTaskUpdate(TaskUpdate.builder()
                .taskId(task.getId())
                .updateUser(user.getUsername())
                .build());
        //添加任务人员
        List<TaskUser> taskUsers = new ArrayList<>();
        //判断参数
        if (taskDTO.getManagerUsers() == null || taskDTO.getManagerUsers() == 0) {
            throw new ServiceException(BaseErrorCode.PARAM_ILLEGAL);
        }
        TaskUser managerUser = TaskUser.builder()
                .userId(taskDTO.getManagerUsers())
                .taskId(task.getId())
                .taskPosition(TaskPosition.MANAGER).build();
        taskUsers.add(managerUser);

        //判断参数
        if (taskDTO.getJoinUsers() == null || taskDTO.getJoinUsers().size() == 0) {
            throw new ServiceException(BaseErrorCode.PARAM_ILLEGAL);
        }

        for (Long userId : taskDTO.getJoinUsers()) {
            //排除任务负责人
            if (taskDTO.getManagerUsers().equals(userId)) {
                continue;
            }
            taskUsers.add(TaskUser.builder()
                    .userId(userId)
                    .taskId(task.getId())
                    .taskPosition(TaskPosition.JOIN).build());
        }
        taskUserManager.insertBatch(taskUsers);

        TaskVO taskVO = new TaskVO();

        taskVO.setTaskId(task.getId());
        taskVO.setTaskCurrentState(task.getTaskCurrentState());

        return taskVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object updateTaskState(Long departmentId, Long taskId, TaskDTO taskDTO) {

        Users user = RequestContext.getUserInfo();

        if (!user.getDepartmentId().equals(departmentId)) {
            throw new ServiceException(BaseErrorCode.AUTHORITY_ILLEGAL);
        }

        Task task = taskManager.getTask(taskId);

        if (task == null) {
            throw new ServiceException(BaseErrorCode.PARAM_ILLEGAL);
        }
        List<TaskUser> users = taskUserManager.getUserIds(taskId);

        //获取任务负责人和参与人员
        Long managerUserId = null;
        List<Long> joinUserIds = new ArrayList<>();

        for (TaskUser taskUser : users) {
            if (taskUser.getTaskPosition() == TaskPosition.MANAGER) {
                managerUserId = taskUser.getUserId();
            } else {
                joinUserIds.add(taskUser.getUserId());
            }
        }

        if (taskDTO.getTaskCurrentState() != null && !user.getId().equals(managerUserId)) {
            throw new ServiceException(BaseErrorCode.AUTHORITY_ILLEGAL);
        }

        if (taskDTO.getTaskCurrentState() != null && user.getId().equals(managerUserId) && task.getTaskCurrentState() == TaskCurrentState.IN_REVIEW) {
            task.setTaskCurrentState(taskDTO.getTaskCurrentState());
            switch (taskDTO.getTaskCurrentState()) {
                case ADVANCE_FINISH:
                    Long differenceTime = task.getEndTime().getEpochSecond() - Instant.now().getEpochSecond();
                    Double distanceEndTime = Math.ceil((double) (differenceTime / (24 * 3600)));
                    task.setDistanceEndTime(-distanceEndTime.intValue());
                    usersManager.updateUserRank(joinUserIds, 5);
                    break;
                case FINISH:
                    usersManager.updateUserRank(joinUserIds, 2);
                    break;
                case ONE_DELAY:
                    task.setDistanceEndTime(7);
                    usersManager.updateUserRank(joinUserIds, -2);
                    break;
                case TWO_DELAY:
                    task.setDistanceEndTime(14);
                    usersManager.updateUserRank(joinUserIds, -5);
                    break;
                case THREE_DELAY:
                    task.setDistanceEndTime(21);
                    usersManager.updateUserRank(joinUserIds, -20);
                    break;
                default:
                    break;
            }
        }

        if (taskDTO.getTaskSubmitState() != null && !joinUserIds.contains(user.getId())) {
            throw new ServiceException(BaseErrorCode.AUTHORITY_ILLEGAL);
        }

        if (taskDTO.getTaskSubmitState() != null && joinUserIds.contains(user.getId()) && task.getTaskCurrentState() == TaskCurrentState.RUNNING) {
            task.setTaskSubmitState(taskDTO.getTaskSubmitState());
            switch (taskDTO.getTaskSubmitState()) {
                case FINISH_TO_REVIEW:
                    task.setTaskCurrentState(TaskCurrentState.IN_REVIEW);
                    break;
                case STOP:
                    task.setTaskCurrentState(TaskCurrentState.PROVISIONAL);
                    break;
                default:
                    break;
            }
        }

        taskManager.updateTask(task);
        taskUpdateManager.addTaskUpdate(TaskUpdate.builder()
                .taskId(taskId)
                .updateUser(user.getUsername())
                .build());

        return null;
    }

    @Override
    public Object updateTask(Long departmentId, Long taskId, TaskDTO taskDTO) {

        Users user = RequestContext.getUserInfo();

        if (!user.getDepartmentId().equals(departmentId)) {
            throw new ServiceException(BaseErrorCode.AUTHORITY_ILLEGAL);
        }

        Task task = taskManager.getTask(taskId);

        if (task == null) {
            throw new ServiceException(BaseErrorCode.PARAM_ILLEGAL);
        }
        List<TaskUser> users = taskUserManager.getUserIds(taskId);

        //获取任务负责人和参与人员
        Long managerUserId = null;
        List<Long> joinUserIds = new ArrayList<>();

        for (TaskUser taskUser : users) {
            if (taskUser.getTaskPosition() == TaskPosition.MANAGER) {
                managerUserId = taskUser.getUserId();
            } else {
                joinUserIds.add(taskUser.getUserId());
            }
        }

        //任务创建者和任务负责人可以更新任务
        if (!user.getId().equals(managerUserId) || !user.getUsername().equals(task.getCreateUser())) {
            throw new ServiceException(BaseErrorCode.AUTHORITY_ILLEGAL);
        }
        List<Long> userIds = new ArrayList<>(joinUserIds);
        List<Long> delTaskUsers = new ArrayList<>();
        //删除人员
        if (taskDTO.getDelJoinUsers() != null && taskDTO.getDelJoinUsers().size() != 0) {
            for (Long delUserId : taskDTO.getDelJoinUsers()) {
                if (userIds.size() != 0) {
                    if (userIds.contains(delUserId)) {
                        delTaskUsers.add(delUserId);
                        userIds.remove(delUserId);
                    }
                }
            }
            if (delTaskUsers.size() != 0) {
                taskUserManager.removeTaskJoinUsers(taskId, delTaskUsers);
            }
        }

        //添加人员
        List<TaskUser> addTaskUsers = new ArrayList<>();
        if (taskDTO.getAddJoinUsers() != null && taskDTO.getAddJoinUsers().size() != 0) {
            for (Long addUserId : taskDTO.getAddJoinUsers()) {
                //排除原来的任务人员

                if (!userIds.contains(addUserId) && !addUserId.equals(managerUserId)) {
                        addTaskUsers.add(TaskUser.builder()
                                .userId(addUserId)
                                .taskId(taskId)
                                .taskPosition(TaskPosition.JOIN).build());
                        userIds.add(addUserId);
                    }

            }
            //添加新人员
            if (addTaskUsers.size() != 0) {
                taskUserManager.insertBatch(addTaskUsers);
            }
        }

        //更新任务负责人
        if (taskDTO.getManagerUsers() != null && !taskDTO.getManagerUsers().equals(managerUserId)) {
            taskUserManager.removeTaskJoinUsers(taskId, Collections.singletonList(managerUserId));
            taskUserManager.insertBatch(Collections.singletonList(TaskUser.builder()
                    .userId(taskDTO.getManagerUsers())
                    .taskId(taskId)
                    .taskPosition(TaskPosition.MANAGER).build()));
        }

        //更新任务信息
        if (!StringUtils.isEmpty(taskDTO.getName())) {
            task.setName(taskDTO.getName());
        }
        if (!StringUtils.isEmpty(taskDTO.getDescription())) {
            task.setDescription(taskDTO.getDescription());
        }
        if (task.getStartTime() != null) {
            task.setStartTime(taskDTO.getStartTime());
        }
        if (taskDTO.getEndTime() != null) {
            task.setEndTime(taskDTO.getEndTime());
        }
        if (taskDTO.getTotalTime() != null) {
            task.setTotalTime(taskDTO.getTotalTime());
        }
        if (!StringUtils.isEmpty(taskDTO.getCreateUser())) {
            task.setCreateUser(taskDTO.getCreateUser());
        }
        if (taskDTO.getTaskLevel() != null) {
            task.setTaskLevel(taskDTO.getTaskLevel());
        }
        if (taskDTO.getTaskType() != null) {
            task.setTaskType(taskDTO.getTaskType());
        }
        if (taskDTO.getSign() != null) {
            task.setSign(taskDTO.getSign());
        }
        if (taskDTO.getExtFile() != null && taskDTO.getExtFile().size()!=0) {
            task.setExtFile(JsonUtils.toJSON(taskDTO.getExtFile()));
        }

        taskManager.updateTask(task);
        taskUpdateManager.addTaskUpdate(TaskUpdate.builder()
                .taskId(taskId)
                .updateUser(user.getUsername())
                .build());
        return null;
    }

    @Override
    public Object removeTask(Long departmentId, Long taskId) {

        Users user = RequestContext.getUserInfo();

        Task task = taskManager.getTask(taskId);

        if (task == null) {
            throw new ServiceException(BaseErrorCode.PARAM_ILLEGAL);
        }
        //获取任务负责人id
        Long managerUserId = taskUserManager.getManagerUserId(taskId);
        //判断当前用户是否是任务负责人
        if (!user.getId().equals(managerUserId)) {
            throw new ServiceException(BaseErrorCode.AUTHORITY_ILLEGAL);
        }

        //删除任务参与人员
        taskUserManager.removeTaskJoinUsers(taskId, null);
        //删除任务更新记录
        taskUpdateManager.removeTaskUpdate(taskId);
        //删除任务及子任务
        taskManager.removeTask(taskId);
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object getTaskUpdateRecodes(Long departmentId, Long taskId) {

        Users user = RequestContext.getUserInfo();

        if (!user.getDepartmentId().equals(departmentId) && user.getRole().ordinal() != 3) {
            throw new ServiceException(BaseErrorCode.AUTHORITY_ILLEGAL);
        }

        return taskUpdateManager.getTaskUpdateList(taskId);
    }


    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public Object getTaskByDate(String date) {

        Users user = RequestContext.getUserInfo();
        LocalDate startDate;
        if (StringUtils.isEmpty(date)) {
            startDate = LocalDate.now();
        } else {
            startDate = LocalDate.parse(date + "-01");
        }
        //计算月初和月末时间
        LocalDate endDate = startDate.with(TemporalAdjusters.lastDayOfMonth());
        Instant startDateTime = LocalDateTime.of(startDate, LocalTime.MIN).toInstant(ZoneOffset.ofHours(8));
        Instant endDateTime = LocalDateTime.of(endDate, LocalTime.MAX).toInstant(ZoneOffset.ofHours(8));
        //这个月所有相关的任务

        List<Task> taskListByTime = taskManager.getTaskListByTime(user.getId(), startDateTime, endDateTime);

        //判断月初是星期几
        DayOfWeek dayOfWeek = startDate.getDayOfWeek();
        Instant weekStartTime = startDateTime;
        Instant weekLastTime = null;
        //计算这个月第一个星期日的时间戳
        switch (dayOfWeek) {
            case MONDAY:
                weekLastTime = LocalDateTime.of(startDate.plusDays(6), LocalTime.MAX).toInstant(ZoneOffset.ofHours(8));
                break;
            case TUESDAY:
                weekLastTime = LocalDateTime.of(startDate.plusDays(5), LocalTime.MAX).toInstant(ZoneOffset.ofHours(8));
                break;
            case THURSDAY:
                weekLastTime = LocalDateTime.of(startDate.plusDays(4), LocalTime.MAX).toInstant(ZoneOffset.ofHours(8));
                break;
            case WEDNESDAY:
                weekLastTime = LocalDateTime.of(startDate.plusDays(3), LocalTime.MAX).toInstant(ZoneOffset.ofHours(8));
                break;
            case FRIDAY:
                weekLastTime = LocalDateTime.of(startDate.plusDays(2), LocalTime.MAX).toInstant(ZoneOffset.ofHours(8));
                break;
            case SATURDAY:
                weekLastTime = LocalDateTime.of(startDate.plusDays(1), LocalTime.MAX).toInstant(ZoneOffset.ofHours(8));
                break;
            case SUNDAY:
                weekLastTime = LocalDateTime.of(startDate, LocalTime.MAX).toInstant(ZoneOffset.ofHours(8));
                break;
        }

        //记录第几周
        int count = 0;
        List<TaskListVO> taskListVOs = new ArrayList<>();
        while (weekStartTime.getEpochSecond() < endDateTime.getEpochSecond()) {
            //筛选一周的任务
            List<Task> taskListByWeek = new ArrayList<>();
            for (Task task : taskListByTime) {
                if (((task.getDistanceEndTime() <= 0 && task.getStartTime().getEpochSecond() < weekLastTime.getEpochSecond() && task.getEndTime().getEpochSecond() > weekStartTime.getEpochSecond()) ||
                        (task.getDistanceEndTime() > 0 && task.getStartTime().getEpochSecond() < weekLastTime.getEpochSecond() && (task.getEndTime().getEpochSecond() + task.getDistanceEndTime() * 24 * 3600) > weekStartTime.getEpochSecond()))) {
                    taskListByWeek.add(task);
                }
            }

            TaskListVO taskListVO = new TaskListVO();
            taskListVO.setWeek("第" + ++count + "周");
            taskListVO.setTaskList(taskListByWeek);

            taskListVOs.add(taskListVO);

            //计算下一周周一,周日的时间
            weekStartTime = weekStartTime.plusSeconds(7 * 24 * 3600);
            weekLastTime = weekLastTime.plusSeconds(7 * 24 * 3600);
            if (weekLastTime.getEpochSecond() > endDateTime.getEpochSecond()) {
                weekLastTime = endDateTime;
            }
        }

        return taskListVOs;
    }


    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public Object getTaskNumByState(Instant startTime, Instant endTime) {
        Users user = RequestContext.getUserInfo();

        return taskManager.getTaskNumByState(user.getId(), startTime, endTime);
    }

    @Override
    public Object saveDownloadTaskIds(List<TaskDTO> taskDTOList) {
        if(taskDTOList == null || taskDTOList.size() == 0) {
            throw new ServiceException(BaseErrorCode.PARAM_ILLEGAL);
        }
        //去除重复的id
        Set<String> ids = taskDTOList.stream()
                .map(t -> t.getId().toString())
                .collect(Collectors.toSet());
        //转成string数组
        String[] strings = ids.toArray(new String[ids.size()]);
        //暂存在Redis中
        String key = UUID.randomUUID().toString();
        stringRedisTemplate.opsForSet().add(key, strings);
        stringRedisTemplate.expire(key, 5, TimeUnit.MINUTES);
        return key;
    }

    @Override
    public Object getNowadaysTask() {
        Users user = RequestContext.getUserInfo();
        return taskManager.getNowadaysTask(user.getId());
    }

    @Override
    public Workbook downloadTask(String taskIdKey) {
        //从Redis中取出存储的id
        Set<String> stringTaskIds = stringRedisTemplate.opsForSet().members(taskIdKey);
        //判断取出的值是否为null
        if (stringTaskIds == null || stringTaskIds.size() == 0) {
            throw new ServiceException(BaseErrorCode.PARAM_ILLEGAL);
        }
        //取出每个任务详情,存入集合中
        List<TaskVO> list = new ArrayList<>();
        for (String stringTaskId : stringTaskIds) {
            TaskVO taskInfo = taskManager.getTaskInfo(Long.parseLong(stringTaskId));
            if (taskInfo == null) {
                throw new ServiceException(BaseErrorCode.PARAM_ILLEGAL);
            }
            list.add(taskInfo);
        }

        //创建一个xls文件
        HSSFWorkbook workbook = new HSSFWorkbook();
        //创建其中的一张纸
        HSSFSheet sheet = workbook.createSheet("任务列表");
        //创建第一行标题
        createTitle(sheet);

        //写入每个任务详情信息
        int i = 0;
        for (TaskVO taskVO : list) {
            HSSFRow r = sheet.createRow(++i);
            r.createCell(0).setCellValue(taskVO.getTaskType().getValue());
            r.createCell(1).setCellValue(taskVO.getName());
            r.createCell(2).setCellValue(taskVO.getDescription());
            r.createCell(3).setCellValue(taskVO.getStartTime().toString());
            r.createCell(4).setCellValue(taskVO.getEndTime().toString());
            r.createCell(5).setCellValue(taskVO.getTotalTime());
            r.createCell(6).setCellValue(taskVO.getCreateUser());
            r.createCell(7).setCellValue(taskVO.getManagerUsers().getUsername());
            StringBuilder joinUsername = new StringBuilder();
            List<UsersVO> joinUsers = taskVO.getJoinUsers();
            for (int j = 0; j < joinUsers.size(); j++) {
                if (j == joinUsers.size() - 1) {
                    joinUsername.append(joinUsers.get(j).getUsername());
                } else {
                    joinUsername.append(joinUsers.get(j).getUsername()).append(",");
                }
            }
            r.createCell(8).setCellValue(joinUsername.toString());
            r.createCell(9).setCellValue(taskVO.getTaskLevel().getValue());
            r.createCell(10).setCellValue(taskVO.getTaskCurrentState().getValue());
            r.createCell(11).setCellValue(taskVO.getTaskSubmitState().getValue());
        }

        return workbook;
    }

    /**
     * 创建表头
     */
    private void createTitle(HSSFSheet sheet) {

        HSSFRow row = sheet.createRow(0);

        sheet.setColumnWidth(0, 10 * 256);
        sheet.setColumnWidth(1, 20 * 256);
        sheet.setColumnWidth(2, 30 * 256);
        sheet.setColumnWidth(3, 20 * 256);
        sheet.setColumnWidth(4, 20 * 256);
        sheet.setColumnWidth(5, 20 * 256);
        sheet.setColumnWidth(6, 20 * 256);
        sheet.setColumnWidth(7, 20 * 256);
        sheet.setColumnWidth(8, 20 * 256);
        sheet.setColumnWidth(9, 15 * 256);
        sheet.setColumnWidth(10, 20 * 256);
        sheet.setColumnWidth(11, 20 * 256);

        HSSFCell cell;

        cell = row.createCell(0);
        cell.setCellValue("任务类型");

        cell = row.createCell(1);
        cell.setCellValue("任务名称");

        cell = row.createCell(2);
        cell.setCellValue("任务描述");

        cell = row.createCell(3);
        cell.setCellValue("任务开始时间");

        cell = row.createCell(4);
        cell.setCellValue("任务完成时间");

        cell = row.createCell(5);
        cell.setCellValue("工期");

        cell = row.createCell(6);
        cell.setCellValue("任务发起人");

        cell = row.createCell(7);
        cell.setCellValue("任务主要负责人");

        cell = row.createCell(8);
        cell.setCellValue("任务参与人员");

        cell = row.createCell(9);
        cell.setCellValue("任务等级");

        cell = row.createCell(10);
        cell.setCellValue("任务当前状态");

        cell = row.createCell(11);
        cell.setCellValue("任务提交状态");


    }


    private List checkTaskCurrentState(List<TaskVO> taskList) {

        if (taskList != null && taskList.size() != 0) {
            taskList.forEach(t -> {
                if (t.getTaskCurrentState() == NOT_START) {
                    if (t.getStartTime().getEpochSecond() <= Instant.now().getEpochSecond()) {
                        t.setTaskCurrentState(TaskCurrentState.RUNNING);
                    }
                }
            });
        }

        return convertTaskVOTo2D(taskList);
    }

    private List convertTaskVOTo2D(List<TaskVO> taskVOS) {

        taskVOS.sort(Comparator.comparing(TaskVO::getStartTime));
        List<List<TaskVO>> ret = new ArrayList();

        asa:
        for (TaskVO taskVO : taskVOS) {

            for (List<TaskVO> vos : ret) {
                Integer distance = vos.get(vos.size() - 1).getDistanceEndTime();
                Instant endTime = vos.get(vos.size() - 1).getEndTime().plus(2, ChronoUnit.DAYS);

                //计算延期任务
                if (distance > 0) {
                    endTime = endTime.plus(distance, ChronoUnit.DAYS);
                }

                if (endTime.isBefore(taskVO.getStartTime())) {
                    vos.add(taskVO);
                    continue asa;

                }
            }
            List<TaskVO> taskVOS1 = new ArrayList<>();
            taskVOS1.add(taskVO);
            ret.add(taskVOS1);
        }

        return ret;
    }
}
