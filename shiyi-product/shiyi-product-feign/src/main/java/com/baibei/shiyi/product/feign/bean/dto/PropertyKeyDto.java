package com.baibei.shiyi.product.feign.bean.dto;

import com.baibei.shiyi.common.tool.page.PageParam;
import lombok.Data;

/**
 * @author: hyc
 * @date: 2019/9/9 10:30
 * @description:
 */
@Data
public class PropertyKeyDto extends PageParam {
    /**
     * 后台类目id
     */
    private Long typeId;
    /**
     * 后台类目名称
     */
    private String typeName;
    /**
     * 属性名称
     */
    private String propertyName;
}
