package com.baibei.shiyi.product.feign.bean.vo;

import lombok.Data;

/**
 * @author: hyc
 * @date: 2019/9/11 10:02
 * @description:
 */
@Data
public class ParameterListVo {
    /**
     * 属性id
     */
    private Long id;
    /**
     * 后台类目名称
     */
    private String typeName;
    /**
     * 参数名称
     */
    private String parameterName;
    /**
     * 排序
     */
    private Integer seq;
    /**
     * 类目id
     */
    private Long typeId;
    /**
     * 录入方式（date:日期类型，text:文本类型，select:下拉框类型，single:单选类型，multy：多选类型）
     */
    private String parameterType;
    /**
     * 参数值
     */
    private String parameterValue;
    /**
     * 编辑状态
     */
    private String editFlag;
}
