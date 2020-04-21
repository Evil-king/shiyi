package com.baibei.shiyi.trade.feign.bean.vo;

import lombok.Data;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/5/29 11:24 AM
 * @description:
 */
@Data
public class CustomerHoldVo {
    /**
     * 交易商编码
     */
    private String customerNo;

    /**
     * 商品交易编码
     */
    private String productTradeNo;

    /**
     * 商品总量
     */
    private Integer totalCount;

    /**
     * 商品冻结数量
     */
    private Integer frozenCount;

    /**
     * 可卖商品数量
     */
    private Integer canSellCount;
}
