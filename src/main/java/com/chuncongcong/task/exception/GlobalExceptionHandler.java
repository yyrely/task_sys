package com.chuncongcong.task.exception;

import com.chuncongcong.task.model.domain.ResultBean;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletResponse;


/**
 * @author yang
 */

@ControllerAdvice
public class GlobalExceptionHandler  {
    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object defaultExceptionHandler(HttpServletResponse response, Exception e) {
        logger.error("exception info:", e);
        if (e instanceof ServiceException) {
            response.setStatus(((ServiceException) e).getHttpCode());
            return new ResultBean<>(e);
        } else {
            ServiceException exception = new ServiceException(BaseErrorCode.SYSTEM_ERROR, StringUtils.isNotEmpty(e.getMessage()) ? e.getMessage() : BaseErrorCode.SYSTEM_ERROR.getMessage());
            response.setStatus(exception.getHttpCode());
            return new ResultBean<>(exception);
        }
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseBody
    public Object notFoundExceptionHandler() {
        return new ResultBean<>(new ServiceException(BaseErrorCode.API_NOT_FOUND));
    }



}
