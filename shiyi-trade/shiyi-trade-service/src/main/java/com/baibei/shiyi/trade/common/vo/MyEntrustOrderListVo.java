package com.baibei.shiyi.trade.common.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MyEntrustOrderListVo {
    private long entrustId;

    /**
     * 委托单单号
     */
    private String entrustNo;

    /**
     * 交易商编码
     */
    private String customerNo;

    /**
     * 委托价格
     */
    private BigDecimal price;

    /**
     * 委托数量
     */
    private Integer entrustCount;

    private String productTradeNo;
}
