package com.baibei.shiyi.product.feign.bean.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Classname AddCategoryProduct
 * @Description 添加类目商品
 * @Date 2019/9/11 17:54
 * @Created by Longer
 */
@Data
public class AddCategoryProduct {
    /**
     * 类目id
     */
    @NotNull(message = "前台类目id不能为空")
    private Long categoryId;

    /**
     * 上架id
     */
    @NotNull(message = "商品上架id不能为空")
    private Long shelfId;
}
