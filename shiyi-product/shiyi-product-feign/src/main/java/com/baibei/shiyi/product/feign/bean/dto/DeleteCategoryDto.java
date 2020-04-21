package com.baibei.shiyi.product.feign.bean.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Classname DeleteCategoryDto
 * @Description 删除类目
 * @Date 2019/9/11 15:02
 * @Created by Longer
 */
@Data
public class DeleteCategoryDto {
    @NotNull(message = "未指定删除的类目")
    private Long categoryId;
}
