package com.baibei.shiyi.product.feign.bean.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Classname AddProductSkuDto
 * @Description TODO
 * @Date 2019/9/3 17:30
 * @Created by 添加商品属性dto
 */
@Data
public class AddProductSkuDto {

    /**
     * 规格编码
     */
    @NotBlank(message = "规格编码不能为空")
    private String skuNo;

    List<BaseKeyValueDto> skuPropertyList = new ArrayList<>();
    /**
     * 库存
     */
    @NotNull(message = "库存不能为空")
    private BigDecimal stock;


}
