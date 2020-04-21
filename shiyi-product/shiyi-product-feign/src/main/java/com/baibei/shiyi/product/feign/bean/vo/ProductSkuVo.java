package com.baibei.shiyi.product.feign.bean.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ProductSkuVo {
    private Long id;

    private Long productId;

    /**
     * 规格编码
     */
    private String skuNo;

    /**
     * 商品sku(json格式。如：[{"颜色":"红色"},{"尺码":"25"}]
     */
    private String skuProperty;

    /**
     * 平台价
     */
    private BigDecimal platformPrice;

    /**
     * 排序
     */
    private Integer seq;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date modifyTime;

    /**
     * 状态(1:正常，0:删除)
     */
    private Byte flag;

}