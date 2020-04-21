package com.baibei.shiyi.product.feign.bean.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author: hyc
 * @date: 2019/9/12 9:58
 * @description:
 */
@Data
public class AddOrUpdateBrandDto {
    private Long id;
    @NotNull(message = "品牌名称不能为空")
    private String brandName;
    @NotNull(message = "品牌logo不能为空")
    private String logo;
    private Integer seq;
}
