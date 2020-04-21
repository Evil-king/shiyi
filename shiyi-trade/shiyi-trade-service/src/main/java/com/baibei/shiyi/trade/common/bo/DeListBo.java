package com.baibei.shiyi.trade.common.bo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/12/28 11:09
 * @description:
 */
@Data
@Builder
public class DeListBo {
    /**
     * 成交单单号
     */
    private String dealNo;

    /**
     * 关联委托单单号
     */
    private String entrustNo;

    /**
     * 冗余商品交易编码
     */
    private String productTradeNo;

    /**
     * 摘牌方客户编号
     */
    private String delister;

    /**
     * 被摘牌方客户编码
     */
    private String beDelister;

    /**
     * 摘牌方手续费
     */
    private BigDecimal delisterFee;

    /**
     * 被摘牌方手续费
     */
    private BigDecimal beDelisterFee;

    /**
     * 成交类型，buy=摘牌买入，sell=摘牌卖出
     */
    private String direction;

    /**
     * 成交价格
     */
    private BigDecimal price;

    /**
     * 成交数量
     */
    private Integer count;

    /**
     * 成交时间
     */
    private Date createTime;
}