package com.baibei.shiyi.order.common.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/8/13 14:53
 * @description:
 */
@Data
public class MyOrderTempVo {
    private Long id;

    private String orderNo;

    private String status;

    private String productName;

    private String productImg;

    private BigDecimal actualAmount;

    private Date createTime;

    private String type;

    private String skuProperty;

    private Integer quantity;

    private String shelfType;

}