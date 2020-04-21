package com.baibei.shiyi.product.feign.bean.dto;

import com.baibei.shiyi.common.tool.page.PageParam;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Classname AdmCategoryProductDto
 * @Description 前台类目商品dto
 * @Date 2019/9/11 15:55
 * @Created by Longer
 */
@Data
public class AdmProductNotInCategroyDto extends PageParam {
    @NotNull(message = "前台类目id不能为空")
    private Long categoryId;

    private String shelfName;

    private String spuNo;

    private Long typeId;
}
