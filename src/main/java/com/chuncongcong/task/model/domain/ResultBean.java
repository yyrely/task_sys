package com.chuncongcong.task.model.domain;


import com.chuncongcong.task.exception.ServiceException;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by Sion on 2017/9/2.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultBean<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    public final static int SUCCESS = 1;
    public final static int FAIL = 0;

    private int code;
    private T data;
    private String msg;


    public ResultBean() {
        super();
        this.code = SUCCESS;
    }
    public ResultBean(T data) {
        super();
        this.code = SUCCESS;
        this.data = data;
    }
    public ResultBean(Throwable e) {
        super();
        this.msg = e.toString();
        this.code = FAIL ;
        if(e instanceof ServiceException){
            this.msg = e.getMessage();
            this.code = ((ServiceException) e).getCode();
        }
    }


}
