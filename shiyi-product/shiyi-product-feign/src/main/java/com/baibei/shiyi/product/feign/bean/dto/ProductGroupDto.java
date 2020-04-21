package com.baibei.shiyi.product.feign.bean.dto;

import com.baibei.shiyi.common.tool.page.PageParam;
import lombok.Data;

import java.util.Set;


@Data
public class ProductGroupDto extends PageParam {

    private Long groupId;

    private Set<String> shelfIds;

    /**
     * 上级商品名称
     */
    private String shelfName;

    /**
     * 商品货号
     */
    private String spuNo;

    /**
     * 后台类目Id
     */
    private Long typeId;
}
