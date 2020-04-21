package com.baibei.shiyi.product.feign.bean.vo;

import lombok.Data;

/**
 * @Classname ProductParamVo
 * @Description 商品参数vo
 * @Date 2019/8/13 18:28
 * @Created by Longer
 */
@Data
public class ProductParamVo {
    /**
     * 参数名
     */
    private String name;
    /**
     * 参数值
     */
    private String value;
}
