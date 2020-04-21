package com.baibei.shiyi.trade.feign.bean.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class HoldStatisticalVo {

    /**
     * 客户编号
     */
    private String customerNo;

    /**
     * 实名名称
     */
    private String customerName;

    /**
     * 商品交易编码
     */
    private String productTradeNo;


    /**
     * 商品交易名称
     */
    private String productName;

    /**
     * 商品总数量
     */
    private Integer remaindCount;

    /**
     * 商品冻结数量
     */
    private Integer frozenCount;

    /**
     * 可卖商品数量
     */
    private Integer canSellCount;

    /**
     * 锁定数量
     */
    private Integer lockCount;

    /**
     * 成本价
     */
    private BigDecimal costPrice;

    /**
     * 最新价
     */
    private BigDecimal lastPrice;

    /**
     * 持仓市值
     */
    private BigDecimal holdMarketValue;


    /**
     * 盈亏资金
     */
    private BigDecimal profitAndLossPrice;


    /**
     * 盈亏比例
     */
    private BigDecimal ProfitLossRatio;

}
