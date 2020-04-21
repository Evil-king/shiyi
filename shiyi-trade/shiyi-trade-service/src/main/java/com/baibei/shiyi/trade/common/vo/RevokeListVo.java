package com.baibei.shiyi.trade.common.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RevokeListVo {
    private String productName;

    private String productTradeNo;

    private String result;

    private String resultDesc;

    private String direction;//方向

    private String directionDesc;

    private BigDecimal price;

    private BigDecimal revokeCount;//可撤销数量

    private BigDecimal totalPrice;//可撤销数量*price

    private String entrust_no;//委托单号

    private String createTime;

    private String productImg;
}
