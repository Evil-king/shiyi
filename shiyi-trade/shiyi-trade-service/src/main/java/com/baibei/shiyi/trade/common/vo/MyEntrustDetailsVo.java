package com.baibei.shiyi.trade.common.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MyEntrustDetailsVo {

    private String productName;//商品名称

    private String productTradeNo;//商品交易编码

    private String customerNo;//挂牌方

    private BigDecimal price;//委托价格

    private int entrustCount;//委托数量

    private String entrustNo;//委托单号
}
