package com.baibei.shiyi.order.feign.base.dto;

import lombok.Data;

@Data
public class AfterSaleOrderDto {
    private String orderNo;//主订单编号
    private String orderItemNo;//子订单编号
}
