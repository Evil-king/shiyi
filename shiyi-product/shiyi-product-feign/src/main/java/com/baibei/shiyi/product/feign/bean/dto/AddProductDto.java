package com.baibei.shiyi.product.feign.bean.dto;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Classname AddProductDto
 * @Description TODO
 * @Date 2019/9/3 16:43
 * @Created by 新增商品dto
 */
@Data
public class AddProductDto {
    private Long productId;

    @NotBlank(message = "商品货号不能为空")
    private String spuNo;
    @NotBlank(message = "商品名称不能为空")
    private String productName;

    private List<String> productImgs = new ArrayList<>();

    /**
     * 商品主图
     */
    private String mainImg;
    /**
     * 商品描述
     */
    private String productDesc;

    @NotNull(message = "后台类目不能为空")
    private Long typeId;

    @NotNull(message = "品牌不能为空")
    private Long brandId;

    private String content;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 基础销量
     */
    private BigDecimal commonSellCount;

    /**
     *  已选的属性id列表
     */
    private List<Long> propIds = new ArrayList<>();

    /**
     * 已选的参数列表
     */
    private List<Long> paramIds = new ArrayList<>();

    /**
     * 已选sku信息
     * 用于数据回显。
     */
    private List<BaseKeyValueDto> selectedSkuList = new ArrayList<>();

    /**
     * 商品参数
     */
    private List<BaseKeyValueDto> parameterList = new ArrayList<>();


    private List<AddProductSkuDto> productSkuDtoList =  new ArrayList<>();
}
