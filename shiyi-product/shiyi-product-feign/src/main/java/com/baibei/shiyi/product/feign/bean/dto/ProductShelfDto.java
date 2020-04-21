package com.baibei.shiyi.product.feign.bean.dto;

import com.baibei.shiyi.common.tool.page.PageParam;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@Data
public class ProductShelfDto extends PageParam {

    private Long shelfId;

    @NotNull(message = "商品名称不能为空")
    private String shelfName;

    /**
     * 商品主图
     */
    private String productImg;

    /**
     * 商品货号
     */
    private String spuNo;

    /**
     * 上架编码
     */
    private String shelfNo;

    /**
     * 最低价格
     */
    private BigDecimal minShelfPrice;

    /**
     * 划线价
     */
    private BigDecimal linePrice;

    /**
     * 商品来源
     */
    private String source;

    /**
     * 商品Id
     */
    private Long productId;

    /**
     * 商品描述
     */
    private String productDesc;

    /**
     * 上架时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date shelfTime;

    /**
     * 运费模板
     */
    private String freightType;

    /**
     * 分类
     */
    private String category;

    /**
     * 分类集合
     */
    private List<String> categoryList;

}
