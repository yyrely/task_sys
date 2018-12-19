package com.chuncongcong.task.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hu
 * @date 2018/8/21 16:02
 */


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TipVO {

    private List<TaskVO> taskList = new ArrayList<>();

    private List<MessageBoardVO> messageList = new ArrayList<>();

}
