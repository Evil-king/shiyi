package com.baibei.shiyi.trade.common.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class MyHoldVo{
    private String productName;//商品名称

    private String productTradeNo;//商品交易编码

    private Integer maxSellNum;//可挂数量

    private BigDecimal issuePrice;//最新价
}
