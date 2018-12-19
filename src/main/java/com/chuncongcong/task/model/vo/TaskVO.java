package com.chuncongcong.task.model.vo;

import com.chuncongcong.task.model.constants.*;
import com.chuncongcong.task.model.entity.TaskUpdate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

/**
 * @author Hu
 * @date 2018/8/7 15:36
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskVO {

    private Long taskId;

    private Long parentId;

    private Long departmentId;

    private Long userId;

    private String name;

    private String description;

    private Instant startTime;

    private Instant endTime;

    private Integer totalTime;

    private Integer distanceEndTime;

    private String createUser;

    private UsersVO managerUsers;

    private List<UsersVO> joinUsers;

    private TaskLevel taskLevel;

    private TaskCurrentState taskCurrentState;

    private TaskSubmitState taskSubmitState;

    private TaskType taskType;

    private SignType sign;

    private List<String> extFile;

    private List<TaskUpdate> taskUpdates;



}
