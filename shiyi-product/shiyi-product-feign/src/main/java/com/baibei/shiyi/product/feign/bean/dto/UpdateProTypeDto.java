package com.baibei.shiyi.product.feign.bean.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author: hyc
 * @date: 2019/9/6 11:42
 * @description:
 */
@Data
public class UpdateProTypeDto {
    @NotNull(message = "ID不能为空")
    private Long id ;
    @NotNull(message = "请填写类目名称")
    private String typeName;
    @NotNull(message = "库存数量不能为空")
    private String stockUnit;
}
