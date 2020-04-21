package com.baibei.shiyi.order.common.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AfterSaleOrderVo {
    private String orderNo;
    private String orderItemNo;
    private String serverNo;
    private String createTime;
    private String finishTime;
    private String customerNo;
    private BigDecimal totalAmount;
    private String type;
    private String status;
    private String modifyTime;
}
