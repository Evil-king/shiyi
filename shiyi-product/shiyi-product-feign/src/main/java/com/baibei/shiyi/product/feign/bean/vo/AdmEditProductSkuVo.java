package com.baibei.shiyi.product.feign.bean.vo;

import com.baibei.shiyi.product.feign.bean.dto.BaseKeyValueDto;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Classname AdmProductVo
 * @Description 后台编辑商品sku vo
 * @Date 2019/9/2 18:19
 * @Created by Longer
 */
@Data
public class AdmEditProductSkuVo {
    /**
     * 规格id
     */
    private String skuId;

    /**
     * 规格编码
     */
    private String skuNo;

    /**
     * 规格描述
     */
    private String skuProperty;


    List<BaseKeyValueDto> skuPropertyList = new ArrayList<>();
    /**
     * 库存
     */
    private BigDecimal stock;


}
