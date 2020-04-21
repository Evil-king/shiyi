package com.baibei.shiyi.product.feign.bean.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Classname AddPropertyuDto
 * @Description 添加属性dto
 * @Date 2019/8/1 14:02
 * @Created by Longer
 */
@Data
public class AddPropertyuDto {
    /**
     * 关联后台类目名称
     */
    @NotNull(message = "后台类目不能为空")
    private Long typeId;

    /**
     * 属性名称
     */
    @NotBlank(message = "属性名称不能为空")
    private String propertyName;

    /**
     * 属性可选值列表。逗号分隔
     */
    @NotBlank(message = "属性值不能为空")
    private String propertyValue;

    /**
     * 排序
     */
    private Integer seq;
}
