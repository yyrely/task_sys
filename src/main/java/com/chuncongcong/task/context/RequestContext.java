package com.chuncongcong.task.context;

import com.chuncongcong.task.model.entity.Users;

/**
 * @author Hu
 * @date 2018/9/21 9:44
 */


public class RequestContext {


    private static ThreadLocal<Long> REQUEST_TIME = new InheritableThreadLocal<>();
    private static ThreadLocal<Users> USER_INFO = new InheritableThreadLocal<>();

    private RequestContext() {
    }

    public static Users getUserInfo() {
        return USER_INFO.get();
    }

    public static void setUserInfo(Users users) {
        USER_INFO.set(users);
    }

    public static Long getRequestTime() {
        return REQUEST_TIME.get();
    }

    public static void setRequestTime(Long requestTime) {
        REQUEST_TIME.set(requestTime);
    }

    public static void clear() {
        REQUEST_TIME.remove();
        USER_INFO.remove();
    }

}
