package com.baibei.shiyi.product.feign.bean.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Classname SpuDto
 * @Description spuDto
 * @Date 2019/9/19 15:53
 * @Created by Longer
 */
@Data
public class SpuDto {

    /**
     * 商品货号
     */
    @NotBlank(message = "未指定商品货号")
    private String spuNo;

}
