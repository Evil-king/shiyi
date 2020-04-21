package com.baibei.shiyi.order.common.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/9/16 14:14
 * @description:
 */
@Data
public class PayDto {
    private String customerNo;
    private String orderNo;
    private BigDecimal actualAmount;
}