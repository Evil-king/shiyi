package com.baibei.shiyi.order.feign.base.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ShiyiCartDto {

    /**
     * 以逗号分离购物车Id
     */
    @NotNull
    private String cartIds;
}
