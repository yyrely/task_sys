package com.chuncongcong.task.model.constants;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author Hu
 * @date 2018/8/15 14:18
 */


public enum PraiseType {

    /**
     * 0.未知
     */
    UNKNOWN("unknown"),
    /**
     * 1.未点赞
     */
    NOT_PRAISE("false"),
    /**
     * 2.已点赞
     */
    ALREADY_PRAISE("true");

    private String value;

    PraiseType(String praise) {
        this.value = praise;
    }

    @JsonValue
    private String getValue() {
        return value;
    }

    @JsonCreator
    private PraiseType create(String value) {
        PraiseType[] praiseTypes = PraiseType.values();
        for (PraiseType praiseType : praiseTypes) {
            if (value.equalsIgnoreCase(praiseType.getValue())) {
                return praiseType;
            }
        }
        return UNKNOWN;
    }

}
