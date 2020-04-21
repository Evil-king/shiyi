package com.baibei.shiyi.trade.feign.bean.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/5/29 11:23 AM
 * @description:
 */
@Data
public class CustomerHoldDto {
    @NotBlank(message = "客户编码不能为空")
    private String customerNo;

    @NotBlank(message = "交易商品编码不能为空")
    private String productTradeNo;
}
