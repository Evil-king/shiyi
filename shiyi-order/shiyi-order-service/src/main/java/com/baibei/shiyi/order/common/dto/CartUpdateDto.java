package com.baibei.shiyi.order.common.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/8/1 10:59
 * @description:
 */
@Data
public class CartUpdateDto {
    @NotNull(message = "ID不能为空")
    private Long id;

    private Long skuId;

    private Integer quantity;
}