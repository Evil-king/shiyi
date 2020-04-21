package com.baibei.shiyi.trade.feign.bean.dto;

import com.baibei.shiyi.common.tool.page.PageParam;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: hyc
 * @date: 2019/11/18 10:31
 * @description:
 */
@Data
public class DealOrderDto extends PageParam {

    /**
     * 成交单号
     */
    private String dealNo;

    private String startTime;

    private String endTime;

    /**
     * 成交类型(buy=摘牌买入,sell=摘牌卖出)
     */
    private String direction;

    /**
     * 摘牌方客户编号
     */
    private String delister;

    /**
     * 被摘牌客户编码
     */
    private String beDelister;

    /**
     * 商品编码
     */
    private String productTradeNo;

    /**
     * 委托单号
     */
    private String entrustNo;

    /**
     * 排除用户
     */
    private String excludeUsers;
}
