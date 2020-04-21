package com.baibei.shiyi.product.feign.bean.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Classname AdmDeleteProductDto
 * @Description 删除商品dto
 * @Date 2019/9/12 9:55
 * @Created by Longer
 */
@Data
public class AdmDeleteProductDto {
    @NotNull(message = "未勾选商品")
    private Long productId;
}
