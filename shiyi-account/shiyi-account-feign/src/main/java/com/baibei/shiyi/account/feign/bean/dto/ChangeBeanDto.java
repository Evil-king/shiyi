package com.baibei.shiyi.account.feign.bean.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: hyc
 * @date: 2019/10/31 19:22
 * @description:
 */
@Data
public class ChangeBeanDto {
    /**
     * 积分修改数
     */
    private BigDecimal beanChangeAmout;
    /**
     * 积分交易类型 RecordBeanTradeTypeEnum ——>可以通过这个类获取
     */
    private String beanTradeType;
    /**
     * 积分收支类型 可以通过常量Retype获取
     */
    private String beanReType;
    /**
     * 积分类型（consumption:消费 exchange:兑换，shiyi:屹家无忧）
     */
    private String beanType;
}
