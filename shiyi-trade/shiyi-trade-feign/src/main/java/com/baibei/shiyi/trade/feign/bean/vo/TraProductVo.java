package com.baibei.shiyi.trade.feign.bean.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TraProductVo {
    /**
     * 主键id
     */
    private Long id;

    /**
     * 货号，商品的唯一编码
     */
    private String spuNo;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品icon
     */
    private String productIcon;

    /**
     * 商品交易编码
     */
    private String productTradeNo;

    /**
     * 交易状态，submit=已提交（创建完还未审核通过）；wait=待上市；onmarket=已上市；trading=交易中；stop=停盘；exit=退市
     */
    private String tradeStatus;

    /**
     * 冗余归属挂牌商
     */
    private String memberNo;

    /**
     * 商品交易名称
     */
    private String productTradeName;

    /**
     * 发行价/最新价
     */
    private BigDecimal issuePrice;

    /**
     * 仓单兑换价
     */
    private BigDecimal exchangePrice;

    /**
     * 最小交易单位
     */
    private Integer minTake;

    /**
     * 最大卖出量
     */
    private Integer maxSellNum;

    /**
     * 上市日期
     */
    private String marketTime;

    /**
     * T+N天(交易解锁时间)
     */
    private Integer tradeFrozenDay;

    /**
     * T+N天(兑换解锁时间)
     */
    private Integer exchangeFrozenDay;

    /**
     * 买入手续费
     */
    private BigDecimal buyFee;

    /**
     * 卖出手续费
     */
    private BigDecimal sellFee;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 修改时间
     */
    private String modifyTime;

    /**
     * 是否删除(1:正常，0:删除)
     */
    private Byte flag;
}