package com.baibei.shiyi.cash.feign.base.dto;

import com.baibei.shiyi.common.tool.page.PageParam;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @Classname Apply1010PagelistDto
 * @Description 查询台账列表dto
 * @Date 2019/11/11 19:50
 * @Created by Longer
 */
@Data
public class Apply1010PagelistDto{
    private Integer currentPage;
    private Integer pageSize;
    private String customerNo;
}
