package com.chuncongcong.task.model.vo;

import com.chuncongcong.task.model.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

/**
 * @author Hu
 * @date 2018/8/15 17:10
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageDriftVO {

    private Long id;

    private Long fromUserId;

    private List<Users> toUser;

    private String message;

    private List<String> extFile;

    private Instant updateTime;
}
