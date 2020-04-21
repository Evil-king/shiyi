package com.baibei.shiyi.trade.common.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: hyc
 * @date: 2019/11/14 19:20
 * @description:
 */
@Data
public class TotalMarketValueVo {
    /**
     * 总持有市值
     */
    private BigDecimal totalMarketValue=BigDecimal.ZERO;
    /**
     * 持有总盈亏
     */
    private BigDecimal totalProfitAndLoss=BigDecimal.ZERO;
}
