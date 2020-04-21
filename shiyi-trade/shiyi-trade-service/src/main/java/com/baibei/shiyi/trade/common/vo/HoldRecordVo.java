package com.baibei.shiyi.trade.common.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class HoldRecordVo {
    private String recordNo;
    private String createTime;
    private String productIcon;
    private String productName;
    private String productTradeNo;
    private String resource;
    private String resourceDesc;
    private BigDecimal price;
    private int count;
    private BigDecimal totalPrice;
}
