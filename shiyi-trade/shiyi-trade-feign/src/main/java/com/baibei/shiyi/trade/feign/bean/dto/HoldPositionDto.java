package com.baibei.shiyi.trade.feign.bean.dto;

import com.baibei.shiyi.common.tool.page.PageParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HoldPositionDto extends PageParam {

    /**
     * 客户编码
     */
    private String customerNo;

    /**
     * 商品交易编码
     */
    private String productTradeNo;

    /**
     * 排除用户
     */
    private String excludeUsers;
}
