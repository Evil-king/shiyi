package com.baibei.shiyi.product.feign.bean.dto;

import com.baibei.shiyi.common.tool.page.PageParam;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author: hyc
 * @date: 2019/9/12 9:56
 * @description:
 */
@Data
public class BrandListDto extends PageParam {
    private String brandName;
}
