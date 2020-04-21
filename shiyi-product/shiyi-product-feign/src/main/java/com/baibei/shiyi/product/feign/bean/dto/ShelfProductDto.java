package com.baibei.shiyi.product.feign.bean.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Classname ShelfProductDto
 * @Description 商品上下架dto
 * @Date 2019/9/18 18:12
 * @Created by Longer
 */
@Data
public class ShelfProductDto {
    /**
     * 状态。shelf=上架，unshelf=下架
     */
    @NotBlank(message = "未指定状态")
    private String status;

    /**
     * 上架商品id
     */
    @NotNull(message = "未指定商品")
    private Long shelfId;
}
