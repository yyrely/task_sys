package com.chuncongcong.task.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author yang
 * @date 2018/10/25
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageVO<T> {

    private Integer pageNum;

    private Integer pageSize;

    private Integer size;

    private Integer total;

    private Integer pages;

    private List<T> list;

}
