package com.baibei.shiyi.product.feign.bean.dto;

import com.baibei.shiyi.common.tool.page.PageParam;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Classname CategoryDto
 * @Description TODO
 * @Date 2019/7/30 18:11
 * @Created by Longer
 */
@Data
public class CategoryDto extends PageParam {
    /**
     * 前端类目id
     */
    @NotNull(message = "未指定类目ID")
    private Long id;

    private Long shelfId;

    private long parentId;

    private Integer maxCount;

    private Integer cutCount;

}
