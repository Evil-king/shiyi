package com.baibei.shiyi.order.common.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class AfterSaleOrderDetailsVo {
    private String serverNo;
    private String type;
    private String status;
    private String orderItemNo;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    private String customerNo;
    private String receiverPhone;
    private String returnReasons;
    private String problemDescription;
    private String photo;


    private String sendLogisticsNo;
    private String sendLogisticsName;

    private BigDecimal refuseMoney;//申请退款金额
    private BigDecimal confirmAmount;//确认退款金额
    private String refuseAccount;//回退账户
    private BigDecimal refuseIntegral;//回退积分
    private String sendBackAddress;//回寄地址

    private String remark;
}
