package com.baibei.shiyi.trade.common.bo;

import com.baibei.shiyi.trade.model.EntrustOrder;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/12/29
 * @description:
 */
@Data
@Builder
public class TradeValidateBo {
    private String customerNo;
    private String productTradeNo;
    private Integer count;
    private BigDecimal totalCost;

    private EntrustOrder entrustOrder;
    // 挂牌价格
    private BigDecimal price;
}
