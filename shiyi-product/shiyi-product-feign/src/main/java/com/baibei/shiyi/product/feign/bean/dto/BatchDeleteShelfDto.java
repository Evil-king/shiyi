package com.baibei.shiyi.product.feign.bean.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Classname BatchDeleteShelfDto
 * @Description 批量软删除
 * @Date 2019/9/18 18:21
 * @Created by Longer
 */
@Data
public class BatchDeleteShelfDto {
    /**
     * 上架id
     */
    @NotNull(message = "未指定商品")
    private Long shelfId;
}
