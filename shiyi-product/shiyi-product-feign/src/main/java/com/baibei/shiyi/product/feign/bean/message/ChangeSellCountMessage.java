package com.baibei.shiyi.product.feign.bean.message;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @Classname ChangeSellCountMessage
 * @Description 修改销量dto
 * @Date 2019/9/27 14:17
 * @Created by Longer
 */
@Data
public class ChangeSellCountMessage {
    /**
     * 上架编码
     */
    @NotNull(message = "未指定商品")
    private Long shelfId;

    /**
     * 规格编码
     */
    @NotNull(message = "规格ID不能为空")
    private Long skuId;

    /**
     * 增加数量
     */
    @NotNull(message = "修改销量数不能为空")
    private BigDecimal changeAmount;

    /**
     * 加或减标识(in=加；out=减)
     */
    @NotBlank(message = "未指定方向")
    private String retype;
}
