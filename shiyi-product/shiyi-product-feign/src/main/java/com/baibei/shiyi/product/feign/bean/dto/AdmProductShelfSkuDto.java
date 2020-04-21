package com.baibei.shiyi.product.feign.bean.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @Classname AdmProductShelfSkuDto
 * @Description 商品上下架商品规格dto
 * @Date 2019/9/2 18:09
 * @Created by Longer
 */
@Data
public class AdmProductShelfSkuDto {

    /**
     * 上架属性规格id
     */
    @NotNull(message = "上架属性规格不能为空")
    private Long skuId;
    /**
     * 上架价格
     */
    @NotNull(message = "上架价格不能为空")
    private BigDecimal shelfPrice;


}
