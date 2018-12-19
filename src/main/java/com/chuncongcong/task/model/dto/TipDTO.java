package com.chuncongcong.task.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yang
 * @date 2018/10/19
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TipDTO {

    private Boolean taskTip;

    private Boolean messageTip;
}
