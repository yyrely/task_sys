package com.chuncongcong.task.model.vo;

import com.chuncongcong.task.model.constants.TaskCurrentState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskStateVO {

    private TaskCurrentState taskCurrentState;

    private Integer num;
}
