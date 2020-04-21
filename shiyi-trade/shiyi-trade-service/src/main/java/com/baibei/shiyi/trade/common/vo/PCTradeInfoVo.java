package com.baibei.shiyi.trade.common.vo;

import com.baibei.shiyi.quotation.feign.bean.model.PricePosition;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: 会跳舞的机器人
 * @date: 2020/1/2 10:25
 * @description:
 */
@Data
public class PCTradeInfoVo {
    private String productTradeNo;
    private String productName;
    // 最新价
    private BigDecimal latestPrice;
    // 涨停价
    private BigDecimal increaseLimitPrice;
    // 跌停价
    private BigDecimal fallLimitPrice;
    // 可挂数量
    private Integer count;

    // 可用资金
    private BigDecimal balance;

    // 可提资金
    private BigDecimal withdrawableCash;

    // 存货价值
    private BigDecimal worth;

    /**
     * 挂买档位集合（前3档）
     */
    private List<PricePosition> buyPricePositionList = new ArrayList<>();

    /**
     * 挂卖档位集合（前3档）
     */
    private List<PricePosition> sellPricePositionList = new ArrayList<>();

}