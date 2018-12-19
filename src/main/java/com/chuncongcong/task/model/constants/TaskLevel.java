package com.chuncongcong.task.model.constants;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author Hu
 * @date 2018/8/3 17:33
 */
public enum TaskLevel {

    /**
     * 0.未知
     */
    UNKNOWN("未知"),

    /**
     * 1.S级
     */
    S("S级"),

    /**
     * 2.A级
     */
    A("A级"),

    /**
     * 3.B级
     */
    B("B级");

    private String value;

    TaskLevel(String level) {
        this.value = level;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    private TaskLevel create(String value) {
        TaskLevel[] taskLevels = TaskLevel.values();
        for (TaskLevel taskLevel : taskLevels) {
            if (value.equalsIgnoreCase(taskLevel.getValue())) {
                return taskLevel;
            }
        }
        return UNKNOWN;
    }
}
