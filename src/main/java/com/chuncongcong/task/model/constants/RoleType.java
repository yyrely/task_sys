package com.chuncongcong.task.model.constants;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum RoleType {
    /**
     * 0.未知
     */
    UNKNOWN("未知"),

    /**
     * 1.普通
     */
    NORMAL("普通"),

    /**
     * 2.主管
     */
    CHARGE("主管"),

    /**
     * 3.总经理
     */
    GENERAL_MANAGER("总经理");




    private String value;

    RoleType(String role) {
        this.value = role;
    }

    @JsonValue
    private String getValue(){
        return value;
    }

   @JsonCreator
    private RoleType create(String value){
        RoleType[] roleTypes = RoleType.values();
        for (RoleType roleType : roleTypes) {
            if (value.equalsIgnoreCase(roleType.getValue())){
                return roleType;
            }
        }
        return UNKNOWN;
    }
}
