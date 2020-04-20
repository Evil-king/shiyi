package com.baibei.shiyi.account.feign.bean.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: hyc
 * @date: 2019/11/1 11:15
 * @description:
 */
@Data
public class CheckFundType {
    /**
     * 资金类型（money:资金，consumption:消费积分 exchange:兑换积分，shiyi:屹家无忧积分）
     */
    private String fundType;
    private BigDecimal changeAmount;
}
