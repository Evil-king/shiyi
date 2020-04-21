package com.baibei.shiyi.order.common.dto;

import com.baibei.shiyi.common.tool.bean.CustomerBaseDto;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/8/2 14:42
 * @description:
 */
@Data
public class OrderPayInfoDto extends CustomerBaseDto {
    @NotNull(message = "商品信息不能为空")
    private List<ProductDto> productList;
}