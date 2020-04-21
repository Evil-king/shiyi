package com.baibei.shiyi.product.feign.bean.dto;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @Classname ShelfBeanRefDto
 * @Description 上架商品积分类型
 * @Date 2019/10/30 18:18
 * @Created by Longer
 */
@Data
public class ShelfBeanRefDto {
    /**
     * 积分类型（consumption=消费积分；exchange=兑换积分；shiyi=屹家无忧）
     */
    @NotBlank(message = "积分类型不能为空")
    private String beanType;

    /**
     * 上架id
     */
    private Long shelfId;

    /**
     * 单位（percent=百分比；integral=积分）
     */
    @NotBlank(message = "积分单位不能为空")
    private String unit;

    /**
     * 值
     */
    @DecimalMin(value = "0",message = "积分值不能低于0")
    @NotNull(message = "积分值不能为空")
    private BigDecimal value;
}
