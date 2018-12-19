package com.chuncongcong.task.service;

import com.chuncongcong.task.model.dto.TipDTO;
import com.chuncongcong.task.model.dto.UserDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface UserService {


    /**
     * 注册
     * @param userDTO
     * @return
     */
    Object register(UserDTO userDTO) throws Exception;

    /**
     * 登录
     *
     * @param userDTO
     * @return
     */
    Object login(UserDTO userDTO) throws Exception;

    /**
     * 登出
     *
     * @return
     */
    Object logout();

    /**
     * 获取个人信息
     * @param userId
     * @return
     */
    Object getUser(Long userId);

    /**
     * 用户列表
     *
     * @param username
     * @return
     */
    Object getUserList(String username, Long[] departmentIds);


    /**
     * 更新用户信息
     * @param userDTO
     * @return
     */
    Object updateUser(Long userId, UserDTO userDTO) throws Exception;

    /**
     * 验证用户名是否存在
     * @param username
     * @return
     */
    Object checkUsername(String username);

    /**
     * 更新用户点赞数
     * @param userId
     * @return
     */
    Object updateUserPraise(Long userId);

    /**
     * 每日登陆加经验
     * @param userId
     */
    void loginSuccessAddRank(Long userId);

    /**
     * 今日提醒信息
     * @return
     */
    Object getTip();

    /**
     * 改变每日提醒状态
     * @return
     */
    Object updateTip(TipDTO tipDTO);



}
