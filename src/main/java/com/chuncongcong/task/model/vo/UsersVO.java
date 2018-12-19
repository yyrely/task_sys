package com.chuncongcong.task.model.vo;


import com.chuncongcong.task.model.constants.*;
import com.chuncongcong.task.model.entity.Department;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsersVO {

    private Long id;

    private String username;

    private String password;

    private String portrait;

    private Department department;

    //任务中的角色
    private TaskPosition taskPosition;

    private RoleType role;

    private String position;

    private GenderType gender;

    private Instant birthday;

    private String mobilePhone;

    private String landlinePhone;

    private Integer rank;

    private OnlineType isOnline;

    private Integer praise;

    private PraiseType isPraise;

    private List taskVOList;
}
