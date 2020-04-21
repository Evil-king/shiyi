package com.baibei.shiyi.order.common.dto;

import com.baibei.shiyi.common.tool.bean.CustomerBaseDto;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/8/6 10:50
 * @description:
 */
@Data
public class CartValidateDto extends CustomerBaseDto {
    @NotNull(message = "商品信息不能为空")
    private List<ProductNoDto> productList;
}