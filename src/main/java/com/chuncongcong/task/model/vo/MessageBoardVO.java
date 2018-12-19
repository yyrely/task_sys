package com.chuncongcong.task.model.vo;

import com.chuncongcong.task.model.constants.ReadType;
import com.chuncongcong.task.model.constants.SignType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

/**
 * @author Hu
 * @date 2018/8/13 10:58
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageBoardVO {

    private Long id;

    private Long parentId;

    private Long fromUserId;

    private String fromUserName;

    private Long toUserId;

    private String toUserName;

    private String portrait;

    private String message;

    private ReadType isRead;

    private SignType sign;

    private List<String> extFile;

    private Instant createTime;

    private Instant updateTime;

    private List<MessageBoardVO> comments;


}

















