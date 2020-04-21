package com.baibei.shiyi.product.feign.bean.dto;

import com.baibei.shiyi.common.tool.page.PageParam;
import lombok.Data;

/**
 * @author: hyc
 * @date: 2019/9/11 10:01
 * @description:
 */
@Data
public class ParameterListDto extends PageParam {
    /**
     * 后台类目id
     */
    private Long typeId;
    /**
     * 后台类目名称
     */
    private String typeName;
    /**
     * 参数名称
     */
    private String parameterName;
}
