package com.chuncongcong.task.model.constants;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author Hu
 * @date 2018/8/17 15:59
 */


public enum OnlineType {

    /**
     * 0.未知
     */
    UNKNOWN("unknown"),

    /**
     * 1.未登录
     */
    NOT_ONLINE("false"),

    /**
     * 2.登录
     */
    ONLINE("true");

    private String value;

    OnlineType(String online) {
        this.value = online;
    }

    @JsonValue
    private String getValue() {
        return value;
    }

    @JsonCreator
    private OnlineType create(String value) {
        OnlineType[] onlineTypes = OnlineType.values();
        for (OnlineType onlineType : onlineTypes) {
            if (value.equalsIgnoreCase(onlineType.getValue())) {
                return onlineType;
            }
        }
        return UNKNOWN;
    }
}
