package com.baibei.shiyi.trade.feign.bean.dto;

import com.baibei.shiyi.common.tool.page.PageParam;
import lombok.Data;

/**
 * @author: hyc
 * @date: 2019/11/14 14:00
 * @description:
 */
@Data
public class CustomerHoldPageListDto extends PageParam {
    //用户编号
    private String customerNo;
    //开始时间
    private String startTime;
    //结束时间
    private String endTime;

    // 商品编码
    private String productNo;

    // 状态
    private String status;

    // 来源
    private String resource;

    // 排除用户
    private String excludeUser;

}
