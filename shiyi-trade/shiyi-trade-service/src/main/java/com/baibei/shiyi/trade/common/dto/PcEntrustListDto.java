package com.baibei.shiyi.trade.common.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PcEntrustListDto {

    // 客户编码
    @NotBlank(message = "客户编码不能为空")
    private String customerNo;

    private String direction;

    private String productTradeNo;
}
