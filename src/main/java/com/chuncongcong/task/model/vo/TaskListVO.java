package com.chuncongcong.task.model.vo;

import com.chuncongcong.task.model.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

/**
 * @author Hu
 * @date 2018/11/6 15:55
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskListVO {

    private String week;
    private List<Task> taskList;
}
