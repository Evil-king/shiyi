package com.baibei.shiyi.product.feign.bean.vo;

import lombok.Data;

/**
 * @author: hyc
 * @date: 2019/9/6 9:44
 * @description:
 */
@Data
public class ProTypeVo {
    private Long typeId;
    private String title;
    private Integer propertyAmount;
    private Integer parameterAmount;
    private String stockUnit;
}
