package com.baibei.shiyi.trade.common.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author: hyc
 * @date: 2019/11/14 19:45
 * @description:
 */
@Data
@Accessors(chain = true)
public class HoldDetailListVo {

    private long id;

    private String customerNo;

    private String productTradeNo;

    private String productName;
    //可卖量
    private BigDecimal canSellCount;
    //持有量
    private BigDecimal remaindCount;
    //成本价
    private BigDecimal costPrice;
    //最新价
    private BigDecimal productNewPrice;
    /**
     * 持仓市值（商品剩余数量（含冻结）*商品最新价格）
     */
    private BigDecimal marketValue;
    /**
     * 盈亏（商品剩余数量（含冻结）*（商品最新价格-买入价））
     */
    private BigDecimal profitAndLoss;

    private String productImg;

    private BigDecimal lastPrice;

}
