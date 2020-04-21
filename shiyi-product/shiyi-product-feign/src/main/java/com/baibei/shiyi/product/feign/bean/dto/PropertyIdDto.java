package com.baibei.shiyi.product.feign.bean.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author: hyc
 * @date: 2019/9/12 11:19
 * @description:
 */
@Data
public class PropertyIdDto {
    @NotNull(message = "属性不能为空")
    private Long id;
}
