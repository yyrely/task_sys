package com.chuncongcong.task.controller;


import com.chuncongcong.task.model.dto.TipDTO;
import com.chuncongcong.task.model.dto.UserDTO;
import com.chuncongcong.task.model.vo.TokenVO;
import com.chuncongcong.task.service.UserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 登录
     *
     * @param userDTO
     * @return
     */
    @PostMapping("/login")
    public Object login(@RequestBody UserDTO userDTO) throws Exception {
        TokenVO login = (TokenVO) userService.login(userDTO);
        userService.loginSuccessAddRank(login.getUsersVO().getId());
        return login;
    }


    /**
     * 验证用户名
     *
     * @param username
     * @return
     */
    @PostMapping("/check")
    public Object checkUsername(
            @RequestParam("username") String username){
        return userService.checkUsername(username.trim());
    }


    /**
     * 注册
     * @param userDTO
     * @return
     * @throws Exception
     */
    @PostMapping("/register")
    public Object register(@RequestBody UserDTO userDTO) throws Exception {
        return userService.register(userDTO);
    }

    /**
     * 登出
     * @return
     */
    @PostMapping("/logout")
    public Object logout() {
        return userService.logout();
    }

    /**
     * 用户列表根据用户名
     *
     * @return
     */
    @GetMapping
    public Object getUserList(
            @RequestParam(value = "username", defaultValue = "") String username,
            @RequestParam(value = "departmentIds", defaultValue = "") Long[] departmentIds) {
        return userService.getUserList(username.trim(), departmentIds);
    }

    /**
     * 修改个人信息
     * @param userDTO
     * @return
     */
    @PutMapping("/{userId}")
    public Object updateUser(
            @PathVariable("userId") Long userId,
            @RequestBody UserDTO userDTO) throws Exception {
        return userService.updateUser(userId, userDTO);
    }


    /**
     * 获取用户信息
     * @param userId
     * @return
     */
    @GetMapping("/{userId}")
    public Object getUser(
            @PathVariable("userId") Long userId){
        return userService.getUser(userId);
    }

    /**
     * 点赞或取消赞功能
     * @param userId
     * @return
     */
    @PutMapping("/{userId}/praise")
    public Object updatePraise(
            @PathVariable("userId") Long userId){
        return userService.updateUserPraise(userId);
    }

    /**
     * 获取每日提醒
     *
     * @return
     */
    @GetMapping("/tips")
    public Object getTip(){
        return userService.getTip();
    }

    /**
     * 更新每日提醒状态
     * @param tipDTO
     * @return
     */
    @PutMapping("/tips")
    public Object updateTip(@RequestBody TipDTO tipDTO) {
        return userService.updateTip(tipDTO);
    }
}
