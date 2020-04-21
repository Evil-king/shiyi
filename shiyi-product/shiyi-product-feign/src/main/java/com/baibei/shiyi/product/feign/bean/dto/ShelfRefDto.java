package com.baibei.shiyi.product.feign.bean.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Classname ShelfRefDto
 * @Description 获取最小单位商品dto
 * @Date 2019/8/1 11:03
 * @Created by Longer
 */
@Data
public class ShelfRefDto{

    /**
     * 上架编码
     */
    @NotNull(message = "上架ID不能为空")
    private Long shelfId;

    /**
     * 规格编码
     */
    private Long skuId;


}
