package com.baibei.shiyi.order.common.dto;

import lombok.Data;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/8/2 14:43
 * @description:
 */
@Data
public class ProductNoDto {
    private Long shelfId;
    private Long skuId;
    private Integer num;
}