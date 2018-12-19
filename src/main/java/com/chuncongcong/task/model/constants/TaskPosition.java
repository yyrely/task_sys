package com.chuncongcong.task.model.constants;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author Hu
 * @date 2018/8/3 17:48
 */
public enum TaskPosition {

    /**
     * 0.未知
     */
    UNKNOWN("未知"),

    /**
     * 1.参与人员
     */
    JOIN("参与人员"),

    /**
     * 2.负责人
     */
    MANAGER("负责人");

    private String value;

    TaskPosition(String position) {
        this.value = position;
    }

    @JsonValue
    private String getValue() {
        return value;
    }

    @JsonCreator
    private TaskPosition create(String value) {
        TaskPosition[] taskPositions = TaskPosition.values();
        for (TaskPosition taskPosition : taskPositions) {
            if (value.equalsIgnoreCase(taskPosition.getValue())) {
                return taskPosition;
            }
        }
        return UNKNOWN;
    }
}
