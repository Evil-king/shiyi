package com.baibei.shiyi.trade.feign.bean.dto;

import com.baibei.shiyi.common.tool.bean.CustomerBaseDto;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AdminTradeConfigDto extends CustomerBaseDto {

    private Long id;

    /**
     * * T+N天(交易解锁时间)
     */
    private Integer tradeFrozenDay;

    /**
     * T+N(兑换)
     */
    private Integer exchangeFrozenDay;

    /**
     * 收盘价计算
     */
    private Integer closingPriceCalculation;

    /**
     * 买入手续费
     */
    private BigDecimal buyFee;

    /**
     * 卖出手续费
     */
    private BigDecimal sellFee;

    /**
     * 最优价成交，1=是，0=否
     */
    private Byte bestPriceDeal;

    /**
     * 行情价是否显示
     */
    private Byte marketAreaShow;

    /**
     * 报价区
     */
    private Byte quotationAreaShow;

    /**
     * 开休市标识（1=开市；0=休市）
     */
    private String tradeTimeFlag;

    /**
     * 早上开市时间
     */
    private Long morningStartTime;

    /**
     * 早上休市时间
     */
    private Long morningEndTime;

    /**
     * 下午开市时间
     */
    private Long afternoonStartTime;

    /**
     * 下午休市时间
     */
    private Long afternoonEndTime;

}
