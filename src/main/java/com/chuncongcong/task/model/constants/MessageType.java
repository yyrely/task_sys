package com.chuncongcong.task.model.constants;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author Hu
 * @date 2018/8/27 16:44
 */


public enum MessageType {
    /**
     * 0.未知
     */
    UNKNOWN("unknown"),

    /**
     * 1.收信
     */
    RECEIVE("receive"),

    /**
     * 2.发送
     */
    SEND("send"),

    /**
     * 3.收藏
     */
    SIGN("sign");

    private String value;

    MessageType(String read) {
        this.value = read;
    }

    @JsonCreator
    public static MessageType create(String value) {
        MessageType[] messageTypes = MessageType.values();
        for (MessageType messageType : messageTypes) {
            if (value.equals(messageType.ordinal() + "")) {
                return messageType;
            } else if (value.equalsIgnoreCase(messageType.getValue())) {
                return messageType;
            }
        }
        return UNKNOWN;
    }

    @JsonValue
    private String getValue() {
        return value;
    }
}
