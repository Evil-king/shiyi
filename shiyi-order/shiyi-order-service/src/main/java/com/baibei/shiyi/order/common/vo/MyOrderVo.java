package com.baibei.shiyi.order.common.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/8/13 11:13
 * @description:
 */
@Data
public class MyOrderVo {
    private String orderNo;

    private String status;

    private String statusText;

    private List<ProductNameImgVo> productList;

    private Date createTime;

    private Integer productCount=0;

    /**
     * 支付方式以及支付金额
     */
    private List<KeyValue> payList;

}