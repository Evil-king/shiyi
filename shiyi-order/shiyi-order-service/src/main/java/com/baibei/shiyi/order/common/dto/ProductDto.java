package com.baibei.shiyi.order.common.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/8/6 13:52
 * @description:
 */
@Data
public class ProductDto {
    private Long shelfId;
    private Long skuId;
    private BigDecimal num;
}