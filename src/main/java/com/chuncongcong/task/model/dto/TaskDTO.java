package com.chuncongcong.task.model.dto;

import com.chuncongcong.task.model.constants.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

/**
 * @author Hu
 * @date 2018/8/8 16:18
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {

    private Long id;

    private Long parentId;

    private String name;

    private String description;

    private Instant startTime;

    private Instant endTime;

    private Integer totalTime;

    private TaskLevel taskLevel;

    private TaskType taskType;

    private SignType sign;

    private TaskCurrentState taskCurrentState;

    private TaskSubmitState taskSubmitState;

    private String createUser;

    private Long managerUsers;

    private List<Long> joinUsers;

    private List<Long> addJoinUsers;

    private List<Long> delJoinUsers;

    private String updateUser;

    private Instant updateTime;

    private List<String> extFile;
}
