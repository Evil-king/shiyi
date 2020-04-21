package com.baibei.shiyi.product.feign.bean.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author: hyc
 * @date: 2019/9/6 10:16
 * @description:
 */
@Data
public class AddProTypeDto {
    @NotNull(message ="请填写类目名称")
    private String typeName;
    private String stockUnit;
}
