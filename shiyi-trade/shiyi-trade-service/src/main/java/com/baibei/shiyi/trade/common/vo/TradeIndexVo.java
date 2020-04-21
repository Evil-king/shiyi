package com.baibei.shiyi.trade.common.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TradeIndexVo {
    private String productName;
    private String productTradeNo;
    /**
     * 最新价
     */
    private String lastPrice;

    /**
     * 开盘价
     */
    private String openPrice;

    /**
     * 最高价
     */
    private String highPrice;

    /**
     * 最低价
     */
    private String lowPrice;

    /**
     * 购价
     */
    private String buyPrice;

    /**
     * 购量
     */
    private String buyCount;

    /**
     * 售价
     */
    private String sellPrice;

    /**
     * 售量
     */
    private String sellCount;

    /**
     * 成交量
     */
    private String dealCount;

    /**
     * 成交额
     */
    private String dealAmount;

    /**
     * 涨跌
     */
    private String updown;

    /**
     * 涨跌幅
     */
    private String updownRate;

    /**
     * 库存量(所有用户该商品持有量的总和)
     */
    private String totalCanSell;
}
