package com.chuncongcong.task.exception;

import lombok.Data;

/**
 * Created by Sion on 2017/9/4.
 */
@Data
public class BaseErrorCode implements ErrorCode {


    /**系统异常*/
    public static final ErrorCode SYSTEM_ERROR = new BaseErrorCode(10, 200, "system error");
    /**auth异常*/
    public static final ErrorCode AUTH_ERROR = new BaseErrorCode(4, 200, " illegally authorized!");
    /**请求不合法*/
    public static final ErrorCode REQUEST_ILLEGAL = new BaseErrorCode(11, 200, "request illegal ");
    /**方法没有响应*/
    public static final ErrorCode METHOD_NOT_ALLOWED = new BaseErrorCode(12, 200, "method not allowed ");
    /**参数异常*/
    public static final ErrorCode PARAM_ILLEGAL = new BaseErrorCode(20, 200, "param illegal");
    /**权限异常*/
    public static final ErrorCode AUTHORITY_ILLEGAL = new BaseErrorCode(21, 200, "authority illegal");
    /**
     * 用户未登录
     */
    public static final ErrorCode NOT_LOGIN = new BaseErrorCode(50, 200, "not login");
    /** 在其他地方登录 */
    public static final ErrorCode OTHER_LOGIN = new BaseErrorCode(51, 200, "other login");

    public static final ErrorCode LOGIN_ILLEGAL = new BaseErrorCode(52, 200, "login illegal");

    public static final ErrorCode SIGN_ERROR = new BaseErrorCode(54, 200, "sign error");

    public static final ErrorCode MAKE_ILLEGAL = new BaseErrorCode(55,200,"make illegal");

    public static final ErrorCode API_NOT_FOUND = new BaseErrorCode(404,200, "api not found");

    public static final ErrorCode SEND_CODE_FAIL = new BaseErrorCode(1006,200,"send code fail");


    private int code;
    private int httpCode;
    private String message;

    public BaseErrorCode(int code, int httpCode, String message) {
        this.httpCode = httpCode;
        this.code = code;
        this.message = message;
    }


}
