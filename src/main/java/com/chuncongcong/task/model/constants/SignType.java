package com.chuncongcong.task.model.constants;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author Hu
 * @date 2018/8/3 17:24
 */
public enum SignType {

    /**
     * 0.未知
     */
    UNKNOWN("unknown"),

    /**
     * 1.未标记
     */
    NOT_SIGN("false"),

    /**
     * 2.标记
     */
    SIGN("true");

    private String value;

    SignType(String sign) {
        this.value = sign;
    }

    @JsonValue
    private String getValue() {
        return value;
    }

    @JsonCreator
    public static SignType create(String value) {
        SignType[] signTypes = SignType.values();
        for (SignType signType : signTypes) {
            if (value.equals(signType.ordinal() + "")) {
                return signType;
            } else if (value.equalsIgnoreCase(signType.getValue())) {
                return signType;
            }
        }
        return UNKNOWN;
    }
}
