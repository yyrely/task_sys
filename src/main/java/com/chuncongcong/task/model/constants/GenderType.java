package com.chuncongcong.task.model.constants;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author Hu
 * @date 2018/8/3 14:45
 */
public enum GenderType {

    /**
     * 0.未知
     */
    UNKNOWN("未知"),

    /**
     * 1.男
     */
    MAN("男"),

    /**
     * 2.女
     */
    WOMAN("女");

    private String value;

    GenderType(String gender) {
        this.value = gender;
    }

    @JsonValue
    private String getValue() {
        return value;
    }

    @JsonCreator
    private GenderType create(String value) {
        GenderType[] genderTypes = GenderType.values();
        for (GenderType genderType : genderTypes) {
            if (value.equalsIgnoreCase(genderType.getValue())) {
                return genderType;
            }
        }
        return UNKNOWN;
    }

}
