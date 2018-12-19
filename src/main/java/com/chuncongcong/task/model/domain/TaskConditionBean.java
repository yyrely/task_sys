package com.chuncongcong.task.model.domain;

import com.chuncongcong.task.model.constants.TaskType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Hu
 * @date 2018/8/20 16:54
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
/**
 * 查询任务列表条件
 */
public class TaskConditionBean {

    private List<Long> departmentIds;

    private List<Long> userIds;

    private Long parentId = 0l;

    private TaskType taskType;
}
