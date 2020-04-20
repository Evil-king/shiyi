package com.baibei.shiyi.cash.feign.base.dto;

import com.baibei.shiyi.common.tool.page.PageParam;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class WithdrawListDto{
    private Integer currentPage;
    private Integer pageSize;
    private String orderNo;
    private String customerNo;
    private String userName;
    private String status;
    private String startTime;
    private String endTime;
    private String externalNo;
    private String effective;
    private String reviewer;
}
