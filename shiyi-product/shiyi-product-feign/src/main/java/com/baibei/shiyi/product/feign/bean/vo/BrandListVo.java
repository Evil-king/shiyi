package com.baibei.shiyi.product.feign.bean.vo;

import lombok.Data;

/**
 * @author: hyc 品牌列表
 * @date: 2019/9/12 9:54
 * @description:
 */
@Data
public class BrandListVo {
    private Long id;
    private String brandName;
    private String logo;
    private Integer seq;
}
