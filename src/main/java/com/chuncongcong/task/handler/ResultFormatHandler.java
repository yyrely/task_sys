package com.chuncongcong.task.handler;

import com.chuncongcong.task.model.domain.ResultBean;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Created by Sion on 2017/9/2.
 */
@Aspect
@Component
public class ResultFormatHandler {


    private static final Logger logger = LoggerFactory.getLogger(ResultFormatHandler.class);


    @Pointcut("execution(* com.chuncongcong.task.controller.*.*(..)) && !execution(* com.chuncongcong.task.controller.DepartmentController.downloadTask(..))")
    public void pointcut() {
    }


    @Around("pointcut()")
    private Object resultFormat(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result;
        Object object = joinPoint.proceed();
        if (!(object instanceof ResponseEntity)) {
            result = new ResultBean<>(object);
        } else {
            result = object;
        }
        return result;
    }


}
