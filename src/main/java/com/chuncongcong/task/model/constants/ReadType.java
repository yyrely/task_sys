package com.chuncongcong.task.model.constants;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author Hu
 * @date 2018/8/10 10:25
 */


public enum ReadType {

    /**
     * 0.未知
     */
    UNKNOWN("unknown"),

    /**
     * 1.未读
     */
    UNREAD("false"),

    /**
     * 2.已读
     */
    READ("true");

    private String value;

    ReadType(String read) {
        this.value = read;
    }

    @JsonValue
    private String getValue() {
        return value;
    }

    @JsonCreator
    public static ReadType create(String value) {
        ReadType[] readTypes = ReadType.values();
        for (ReadType readType : readTypes) {
            if (value.equals(readType.ordinal() + "")) {
                return readType;
            } else if (value.equalsIgnoreCase(readType.getValue())) {
                return readType;
            }
        }
        return UNKNOWN;
    }

}
