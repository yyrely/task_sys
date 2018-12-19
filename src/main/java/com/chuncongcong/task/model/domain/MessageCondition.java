package com.chuncongcong.task.model.domain;

import com.chuncongcong.task.model.constants.ReadType;
import com.chuncongcong.task.model.constants.SignType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Hu
 * @date 2018/8/16 10:08
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageCondition {

    private Long fromUserId;

    private Long toUserId;

    private SignType sign;

    private ReadType read;

}
