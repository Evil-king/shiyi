package com.baibei.shiyi.product.feign.bean.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author: hyc
 * @date: 2019/9/9 15:18
 * @description:
 */
@Data
public class UpdatePropertyDto extends  AddPropertyuDto{
    @NotNull(message = "属性id不能为空")
    private Long id;
}
