package com.chuncongcong.task.model.constants;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author yang
 * @date 2018/10/17
 */
public enum TaskType {

    /**
     * 0.未知
     */
    UNKNOWN("未知"),

    /**
     * 1.任务
     */
    TASK("任务"),

    /**
     * 2.新功能
     */
    NEW_FUNCTION("新功能"),

    /**
     * 3.改进
     */
    IMPROVE("改进"),

    /**
     * 4.bug
     */
    BUG("bug");

    private String value;

    TaskType(String level) {
        this.value = level;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    private TaskType create(String value) {
        TaskType[] taskTypes = TaskType.values();
        for (TaskType taskType : taskTypes) {
            if (value.equalsIgnoreCase(taskType.getValue())) {
                return taskType;
            }
        }
        return UNKNOWN;
    }

}
