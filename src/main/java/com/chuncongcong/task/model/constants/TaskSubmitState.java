package com.chuncongcong.task.model.constants;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author yang
 * @date 2018/10/18
 */
public enum TaskSubmitState {

    /**
     * 0.未知
     */
    UNKNOWN("未知"),

    /**
     * 1.正在进行
     */
    FINISH_TO_REVIEW("已完成并提交审核"),

    /**
     * 2.任务暂停
     */
    STOP("任务暂停"),

    /**
     * 3.任务转移
     */
    REMOVE("任务转移");


    private String value;

    TaskSubmitState(String state) {
        this.value = state;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    private TaskSubmitState create(String value) {
        TaskSubmitState[] taskSubmitStates = TaskSubmitState.values();
        for (TaskSubmitState taskSubmitState : taskSubmitStates) {
            if (value.equalsIgnoreCase(taskSubmitState.getValue())) {
                return taskSubmitState;
            }
        }
        return UNKNOWN;
    }
}
