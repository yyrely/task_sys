package com.chuncongcong.task.model.constants;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author Hu
 * @date 2018/8/3 17:38
 */
public enum TaskCurrentState {

    /**
     * 0.未知
     */
    UNKNOWN("未知"),

    /**
     * 1.正在进行
     */
    RUNNING("正在进行"),

    /**
     * 2.未启动
     */
    NOT_START("未启动"),

    /**
     * 3.审核中
     */
    IN_REVIEW("审核中"),

    /**
     * 4.暂定
     */
    PROVISIONAL("暂定"),

    /**
     * 5.提前完成
     */
    ADVANCE_FINISH("提前完成"),

    /**
     * 6.完成
     */
    FINISH("完成"),

    /**
     * 7.延期等级一
     */
    ONE_DELAY("延期等级一"),

    /**
     * 8.延期等级二
     */
    TWO_DELAY("延期等级二"),

    /**
     * 9.延期等级三
     */
    THREE_DELAY("延期等级三"),

    /**
     * 10.不通过
     */
    NOT_PASS("审核不通过");

    private String value;

    TaskCurrentState(String state) {
        this.value = state;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    private TaskCurrentState create(String value) {
        TaskCurrentState[] taskCurrentStates = TaskCurrentState.values();
        for (TaskCurrentState taskCurrentState : taskCurrentStates) {
            if (value.equalsIgnoreCase(taskCurrentState.getValue())) {
                return taskCurrentState;
            }
        }
        return UNKNOWN;
    }


}
