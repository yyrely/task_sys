package com.chuncongcong.task.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Hu
 * @date 2018/8/15 17:11
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageDriftDTO {

    private Long id;

    private Long fromUserId;

    private List<Long> toUserIds;

    private String message;

    private List<String> extFile;
}
