package com.baibei.shiyi.product.feign.bean.dto;

import com.baibei.shiyi.common.tool.bean.CustomerBaseDto;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class ProductCollectDto extends CustomerBaseDto {

    /**
     * 收藏Id
     */
    private Set<String> ids;

    @NotNull
    private Set<String> shelfIds;

    /**
     * 购物车Id
     */
    private String orderCartIds;
}
