package com.chuncongcong.task.model.dto;

import com.chuncongcong.task.model.constants.SignType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Hu
 * @date 2018/8/15 15:33
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageBoardDTO {

    private Long id;

    private List<Long> toUserIds;

    private String message;

    private SignType sign;

    private List<String> extFile;
}
