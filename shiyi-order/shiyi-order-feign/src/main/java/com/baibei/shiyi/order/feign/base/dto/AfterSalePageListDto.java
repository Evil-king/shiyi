package com.baibei.shiyi.order.feign.base.dto;

import com.baibei.shiyi.common.tool.page.PageParam;
import lombok.Data;

@Data
public class AfterSalePageListDto extends PageParam {
    private String orderNo;//主订单编号
    private String customerNo;//用户编号
    private String createTime;//下单时间
    private String status;//售后状态
    private String orderItemNo;//子订单编号
    private String serverNo;//服务单号
    private String type;//售后类型
}
