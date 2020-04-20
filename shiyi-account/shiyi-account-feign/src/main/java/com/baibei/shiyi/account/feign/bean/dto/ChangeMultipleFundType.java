package com.baibei.shiyi.account.feign.bean.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: hyc
 * @date: 2019/11/4 13:40
 * @description:
 */
@Data
public class ChangeMultipleFundType {
    /**
     * 资金类型（money:资金，consumption:消费积分 exchange:兑换积分，shiyi:屹家无忧积分，mallAccount:商城账户）
     */
    private String fundType;
    /**
     * 收支（in:收入,out：支出）
     */
    private String retype;

    private BigDecimal changeAmount;
    /**
     * 交易类型（根据不同交易类型使用不同的枚举类型）
     * 商城交易类型是RecordBeanTradeTypeEnum，
     * TradeMoneyTradeTypeEnum类型
     */
    private String tradeType;
}
