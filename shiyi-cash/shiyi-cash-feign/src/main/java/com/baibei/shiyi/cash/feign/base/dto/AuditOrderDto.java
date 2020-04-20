package com.baibei.shiyi.cash.feign.base.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 出金审核dto
 */
@Data
public class AuditOrderDto{

    /**
     * 订单号
     */
    @NotBlank(message = "未指定审核单号")
    private String orderNo;

    /**
     * 状态。2=审核通过；6=审核不通过
     */
    @NotBlank(message = "未指定审核状态")
    private String status;

    /**
     * 审核人
     */
    private String reviewer;

}
