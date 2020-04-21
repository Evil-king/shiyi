package com.baibei.shiyi.product.feign.bean.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author: hyc
 * @date: 2019/9/11 13:48
 * @description:
 */
@Data
public class UpdateParameterDto extends AddParameterDto {
    @NotNull(message = "参数id不能为空")
    private Long id;
}
