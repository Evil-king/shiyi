package com.baibei.shiyi.trade.common.dto;

import com.baibei.shiyi.common.tool.bean.CustomerBaseDto;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class MyDealOorderListDto extends CustomerBaseDto {

    // 当前页
    @NotNull(message = "当前页不能为空")
    @Min(value = 1, message = "当前页不能小于1")
    private Integer currentPage;

    // 每页记录数
    @NotNull(message = "每页记录数不能为空")
    @Max(value = 20, message = "每页记录数不能超过20")
    private Integer pageSize;

    // 排序字段
    private String sort;

    // 排序规则,asc/desc
    private String order;


    private String startTime;

    private String endTime;
}
