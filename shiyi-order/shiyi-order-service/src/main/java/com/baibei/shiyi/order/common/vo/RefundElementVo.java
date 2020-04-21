package com.baibei.shiyi.order.common.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RefundElementVo {
    private BigDecimal amount;
    private BigDecimal integrationAmount;
}
