package com.baibei.shiyi.product.feign.bean.vo;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Classname ShelfBeanVo
 * @Description
 * @Date 2019/10/30 19:26
 * @Created by Longer
 */
@Data
public class ShelfBeanVo {
    /**
     * 积分类型（consumption=消费积分；exchange=兑换积分；shiyi=屹家无忧）
     */
    private String beanType;

    /**
     * 上架id
     */
    private Long shelfId;

    /**
     * 单位（percent=百分比；integral=积分）
     */
    private String unit;

    /**
     * 值
     */
    private BigDecimal value;


}
