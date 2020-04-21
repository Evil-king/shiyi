package com.baibei.shiyi.order.common.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AfterSaleGoodsVo {
    private String name;
    private String image;
    private BigDecimal amount;
    private String spuNo;
    private String skuproperty;
    private int quantity;
    private BigDecimal totalAmount;
}
