package com.baibei.shiyi.product.feign.bean.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Classname AdmEditProductShelfPageDto
 * @Description 跳转到编辑上架商品页面
 * @Date 2019/9/3 16:43
 * @Created by Longer
 */
@Data
public class AdmEditProductShelfPageDto {
    @NotNull(message = "未指定商品")
    private Long shelfId;

}
