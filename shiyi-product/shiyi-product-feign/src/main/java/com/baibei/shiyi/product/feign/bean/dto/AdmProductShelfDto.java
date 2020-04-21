package com.baibei.shiyi.product.feign.bean.dto;

import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Classname AdmProductDto
 * @Description 商品上下架dto
 * @Date 2019/9/2 18:09
 * @Created by Longer
 */
@Data
public class AdmProductShelfDto{

    /**
     * 上架商品id
     */
    private Long shelfId;
    /**
     * 商品id
     */
    @NotNull(message = "未指定商品")
    private Long productId;

    /**
     * 货号
     */
    @NotBlank(message = "商品货号不能为空")
    private String spuNo;

    /**
     * 商品上架名称
     */
    @NotBlank(message = "商品上架名称不能为空")
    private String shelfName;

    /**
     * 上架的商品规格列表
     */
    private List<AdmProductShelfSkuDto> shelfSkuList = new ArrayList<>();

    /**
     * 划线价
     */
    private BigDecimal linePrice;

    /**
     * 上架编码
     */
    @NotBlank(message = "上架编码不能为空")
    private String shelfNo;

    /**
     * 商品仓（integralmodule=积分舱，firstmodule=头等舱）
     */
/*    private String module;*/

    /**
     * 配售权
     */
    /*private BigDecimal plan;*/

    /**
     * 积分类型（deliveryintegral=提货积分，comsumeintegral=消费积分，yijiabao=屹家保）
     */
    /*@NotBlank(message = "积分类型不能为空")
    private String integralType;*/

    /**
     * 积分最多抵扣值
     */
    private BigDecimal maxdetuch;

    /**
     * 积分最多抵扣单位(percent=百分比，rmb=人民币)
     */
    private String unit;

    /**
     * 来源（integralmodule=积分舱，firstmodule=头等舱，upmodule=升仓，extendmodule=传承仓，sharemodule=共享仓）
     */
   /* private String source;*/

    /**
     * 前端类目id,多个id用逗号分隔
     */
    private List<AddCategoryProduct> categories= new ArrayList<>();

    /**
     * 分润规则
     */
    private String separetBenefit;

    /**
     * 是否上下架（shelf=已上架，unshelf=未上架）
     */
    @NotBlank(message = "状态不能为空")
    private String status;

    /**
     * 运费模板（free=包邮）
     */
    @NotNull(message = "运费模板不能为空")
    @DecimalMax(value = "10000",message = "运费不能超过10000")
    @DecimalMin(value = "0",message = "运费不能低于0")
    private BigDecimal freightType;

    /**
     * 商品类型(send_integral=赠送积分商品；consume_ingtegral=消费积分商品；transfer_product=交割商品)
     */
    @NotBlank(message = "商品类型不能为空")
    private String shelfType;

    /**
     * 上架商品积分类型
     */
    List<ShelfBeanRefDto> shelfBeanRefDtoList = new ArrayList<>();

}
