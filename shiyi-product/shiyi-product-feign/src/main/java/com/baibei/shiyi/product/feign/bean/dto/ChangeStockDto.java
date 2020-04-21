package com.baibei.shiyi.product.feign.bean.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @Classname ChangeStockDto
 * @Description 修改库存dto
 * @Date 2019/09/17 15:54
 * @Created by Longer
 */
@Data
public class ChangeStockDto {

    /**
     * 商品id
     */
    private Long productId;
    /**
     * 上架编码
     */
    private Long shelfId;

    /**
     * 规格编码
     */
    @NotNull(message = "规格ID不能为空")
    private Long skuId;

    /**
     * 扣减库存数
     */
    @NotNull(message = "扣减库存数不能为空")
    private BigDecimal changeCount;

    /**
     * 流水号
     */
    @NotBlank(message = "流水号不能为空")
    private String orderNo;

    /**
     * 用户编码
     */
    private String operatorNo;

    /**
     * 更改类型（trade=交易；sys=系统）
     */
    @NotBlank(message = "类型不能为空")
    private String changeType;

    /**
     * 执行方向（out=扣减；in=增加）
     */
    @NotBlank(message = "执行方向不能为空")
    private String retype;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否修改销量标识(false=不修改；true=修改)
     */
    private boolean changeSellCountFlag=true;

}
