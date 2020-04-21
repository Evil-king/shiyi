package com.baibei.shiyi.order.common.dto;

import com.baibei.shiyi.common.tool.bean.CustomerBaseDto;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/8/5 10:05
 * @description:
 */
@Data
public class OrderSubmitDto extends CustomerBaseDto {
    @NotNull(message = "收货地址不能为空")
    private Long addressId;

    @NotNull(message = "购买商品信息不能为空")
    private List<ProductDto> productList;

    // 备注
    private String remark;

    // 购物车ID
    private String cardIds;

    /**
     * 支付类型
     * mallAccount=商城账户支付；money=存管账户支付
     */
    @NotBlank(message = "支付类型不能为空")
    private String payWay;
}