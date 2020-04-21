package com.baibei.shiyi.product.feign.bean.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Classname AddCategoryDto
 * @Description 新增前台类目dto
 * @Date 2019/9/11 11:15
 * @Created by Longer
 */
@Data
public class AddCategoryDto {
    /**
     * 类目id
     */
    private Long categoryId;
    /**
     * 父级id
     */
    @NotNull(message = "未指定父级id")
    private Long parentId;
    /**
     * 类目名称
     */
    @NotBlank(message = "未填写类目名称")
    private String title;

    /**
     * 类目图片
     */
    private String img;

    /**
     * 是否末级类目。1=是，0=否
     */
    @NotBlank(message = "是否末级标识不能为空")
    private String end;

    /**
     *  是否隐藏（show：显示；hidden：隐藏）
     */
    @NotBlank(message = "是否显示标识不能为空")
    private String showStatus;

    /**
     * 排序
     */
    private Integer seq;
}
