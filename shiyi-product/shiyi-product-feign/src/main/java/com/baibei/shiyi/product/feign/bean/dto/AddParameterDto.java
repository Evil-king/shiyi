package com.baibei.shiyi.product.feign.bean.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author: hyc
 * @date: 2019/9/11 10:36
 * @description:
 */
@Data
public class AddParameterDto {
    /**
     * 后台类目id
     */
    @NotNull(message = "后台类目不可为空")
    private Long typeId;
    /**
     * 参数名
     */
    @NotNull(message = "参数名不可为空")
    private String parameterName;
    /**
     * 参数类型（date:日期类型，text:文本类型，select:下拉框类型，single:单选类型，multy：多选类型）
     */
    @NotNull(message = "参数类型不可为空")
    private String parameterType;
    /**
     * 参数值（多个值以逗号隔开）
     */
    private String parameterValue;
    /**
     * 排序
     */
    private Integer seq;
}
