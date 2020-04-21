package com.baibei.shiyi.trade.feign.bean.dto;

import com.baibei.shiyi.common.tool.page.PageParam;
import lombok.Data;

/**
 * @author: hyc
 * @date: 2019/11/14 16:06
 * @description:
 */
@Data
public class CustomerEntrustOrderListDto extends PageParam {

    private String customerNo;

    private String direction;

    private String startTime;

    private String endTime;

    private String productTradeNo;

    private String entrustNo;

    private String result;
}
