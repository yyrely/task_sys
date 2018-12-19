package com.chuncongcong.task.model.domain;

import com.chuncongcong.task.model.constants.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Hu
 * @date 2018/9/28 9:07
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DUserBean {

    private Long id;

    private String label;

    private RoleType role;
}
