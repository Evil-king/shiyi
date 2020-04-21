package com.baibei.shiyi.trade.common.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: 会跳舞的机器人
 * @date: 2020/1/2 10:25
 * @description:
 */
@Data
public class TradeInfoVo {
    private String productTradeNo;
    private String productName;
    // 最新价
    private BigDecimal latestPrice;
    // 涨幅
    private String increaseRate;
    // 涨停价
    private BigDecimal increaseLimitPrice;
    // 跌停价
    private BigDecimal fallLimitPrice;
    // 可挂数量
    private Integer count;

}