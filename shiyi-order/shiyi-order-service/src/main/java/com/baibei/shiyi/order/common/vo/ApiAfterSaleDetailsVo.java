package com.baibei.shiyi.order.common.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ApiAfterSaleDetailsVo {
    private String msg;
    private String orderItemNo;
    private String image;
    private String name;
    private String skuproperty;
    private BigDecimal amount;
    private int quantity;
    private String status;
    private String statusRemark;
    private BigDecimal refuseIntegral;
    private BigDecimal refuseMoney;
    private String refuseAccount;
    private String refuseAccountRemark;
    private String shelfType;
    private String shelfTypeRemark;



    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date applicationTime;//提交申请时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date successTime;//审核通过时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date doingTime;//售后收货时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date revokedTime;//撤销时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date finishTime;//进行退款/换货时间
}
