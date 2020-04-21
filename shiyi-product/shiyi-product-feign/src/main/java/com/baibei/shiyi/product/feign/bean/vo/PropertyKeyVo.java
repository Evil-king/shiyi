package com.baibei.shiyi.product.feign.bean.vo;

import lombok.Data;

/**
 * @author: hyc
 * @date: 2019/9/9 10:31
 * @description:
 */
@Data
public class PropertyKeyVo {
    /**
     * 属性id
     */
    private Long id;
    /**
     * 后台类目名称
     */
    private String typeName;
    /**
     * 属性名称
     */
    private String propertyName;
    /**
     * 属性值
     */
    private String propertyValue;
    /**
     * 排序
     */
    private Integer seq;
    /**
     * 类目id
     */
    private Long typeId;
    /**
     * 编辑状态
     */
    private String editFlag;
}
