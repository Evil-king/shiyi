package com.baibei.shiyi.trade.feign.bean.dto;

import com.baibei.shiyi.common.tool.page.PageParam;
import lombok.Data;

@Data
public class HoldStatisticalDto extends PageParam {

    /**
     * 客户编号
     */
    private String customerNo;

    /**
     * 商品编码
     */
    private String productTradeNo;

    /**
     * 排除用户
     */
    private String excludeUser;
}
