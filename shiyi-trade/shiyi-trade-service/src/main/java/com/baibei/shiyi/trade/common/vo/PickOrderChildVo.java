package com.baibei.shiyi.trade.common.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 子订单数据展示层
 */
@Data
public class PickOrderChildVo {

    /**
     * 子订单号
     */
    private String pickChildNo;

    /**
     * 提货数量
     */
    private Integer pickNumber;

    /**
     * 成本价
     */
    private BigDecimal costPrice;

    /**
     * 提货价
     */
    private BigDecimal pickUpPrice;

    /**
     * 物流公司
     */
    private String logisticsCompany;

    /**
     * 物流公司单号
     */
    private String logisticsNo;
}
