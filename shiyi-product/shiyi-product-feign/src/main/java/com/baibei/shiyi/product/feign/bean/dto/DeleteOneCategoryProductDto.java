package com.baibei.shiyi.product.feign.bean.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Classname DeleteOneCategoryProductDto
 * @Description 删除指定类目下某个商品dto
 * @Date 2019/9/11 16:29
 * @Created by Longer
 */
@Data
public class DeleteOneCategoryProductDto {
    @NotNull(message = "未指定类目")
    private Long categoryId;
    @NotNull(message = "未指定商品id")
    private Long shelfId;
}
