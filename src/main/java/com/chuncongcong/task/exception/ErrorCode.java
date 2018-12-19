package com.chuncongcong.task.exception;

/**
 * Created by Sion on 2017/8/9.
 */
public interface ErrorCode {
    int getCode();
    String getMessage();
    int getHttpCode();

}
