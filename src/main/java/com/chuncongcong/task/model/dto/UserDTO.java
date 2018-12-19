package com.chuncongcong.task.model.dto;

import com.chuncongcong.task.model.constants.GenderType;
import com.chuncongcong.task.model.constants.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * @author Hu
 * @date 2018/8/6 10:28
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;

    private String username;

    private String password;

    private String rePassword;

    private String portrait;

    private Long departmentId;

    private RoleType role;

    private String position;

    private GenderType gender;

    private Instant birthday;

    private String mobilePhone;

    private String landlinePhone;

    private String timestamp;
}
