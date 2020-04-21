package com.baibei.shiyi.product.feign.bean.vo;

import com.baibei.shiyi.product.feign.bean.dto.AddProductSkuDto;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Classname AdmProductVo
 * @Description 后台编辑商品vo
 * @Date 2019/9/2 18:19
 * @Created by Longer
 */
@Data
public class AdmEditProductVo {
    private Long productId;
    /**
     * 商品名称
     */
    private String productName;
    /**
     * 货号
     */
    private String spuNo;
    /**
     * 后台类目
     */
    private String typeTitle;
    /**
     * 品牌
     */
    private String brandTitle;
    /**
     * 类目id
     */
    private Long typeId;
    /**
     * 品牌id
     */
    private Long brandId;
    /**
     * 商品主图
     */
    private String mainImg;

    /**
     * 商品描述
     */
    private String productDesc;

    /**
     * 图文详情
     */
    private String content;

    /**
     * 已选属性
     */
    private String selectedSkuText;

    /**
     * 商品参数
     */
    private String parameterText;

    /**
     * 有无属性标识（have=有；notHave=没有）
     */
    private String hasSkuFlag;

    /**
     * 基础销量
     */
    private BigDecimal commonSellCount;

    /**
     * 商品图片
     */
    private List<String> productImgs = new ArrayList<>();

    /**
     * 已选商品属性，用于前端回显
     */
    private List<BaseKeyValueVo> selectedSkuList = new ArrayList<>();

    /**
     * 商品规格属性
     */
    private List<AdmEditProductSkuVo> productSkuDtoList = new ArrayList<>();

    /**
     * 商品参数值
     */
    private List<BaseKeyValueVo> parameterList = new ArrayList<>();


}
