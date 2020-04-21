package com.baibei.shiyi.trade.common.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SellVo {
    private String productTradeNo;
    private String productName;
    private String customerNo;
    private BigDecimal price;
    private int num;
}
