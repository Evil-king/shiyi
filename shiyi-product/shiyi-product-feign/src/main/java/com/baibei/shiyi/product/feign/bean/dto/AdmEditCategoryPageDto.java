package com.baibei.shiyi.product.feign.bean.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Classname AdmEditCategoryPageDto
 * @Description 编辑类目页面接收参数dto
 * @Date 2019/9/12 11:12
 * @Created by Longer
 */
@Data
public class AdmEditCategoryPageDto {
    @NotNull(message = "未指定类目")
    private Long categoryId;
}
