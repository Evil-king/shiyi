package com.baibei.shiyi.trade.common.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MyDealOrderListVo {

    private String dealNo;//成交单号

    private String createTime;

    private String productName;

    private String spuNo;

    private String type;

    private String typeDesc;

    private BigDecimal price;

    private int count;

    private BigDecimal totalPrice;

    private String productImg;

    private String sellCustomerNo;

    private String buyCustomerNo;
}
