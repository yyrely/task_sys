package com.chuncongcong.task.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hu
 * @date 2018/9/25 17:32
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentAndUserBean {

    private Long id;

    private String label;

    private List<DUserBean> userList = new ArrayList<>();
}
