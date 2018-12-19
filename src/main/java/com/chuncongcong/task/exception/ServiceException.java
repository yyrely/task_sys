package com.chuncongcong.task.exception;

/**
 * Created by Sion on 2017/8/9.
 */
public class ServiceException extends RuntimeException {

    private Integer code;
    private Integer httpCode;

    public ServiceException(){

    }

    public ServiceException(int code, int httpCode, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.httpCode = httpCode;
    }

    public ServiceException(int code, int httpCode, String message) {
        super(message);
        this.code = code;
        this.httpCode = httpCode;
    }

    public ServiceException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.httpCode = errorCode.getHttpCode();
    }

    public ServiceException(ErrorCode errorCode , String message ) {
        super(message);
        this.code = errorCode.getCode();
        this.httpCode = errorCode.getHttpCode();
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(Integer httpCode) {
        this.httpCode = httpCode;
    }
}
