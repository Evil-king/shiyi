package com.baibei.shiyi.order.common.dto;

import com.baibei.shiyi.common.tool.bean.CustomerBaseDto;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/8/1 10:16
 * @description:
 */
@Data
public class CartAddDto extends CustomerBaseDto {
    @NotNull(message = "上架商品编号不能为空")
    private Long shelfId;

    @NotNull(message = "数量不能为空")
    private Integer quantity;

    @NotNull(message = "商品规格不能为空")
    private Long skuId;

}