package com.baibei.shiyi.trade.common.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/12/26 18:26
 * @description:
 */
@Data
public class
EntrustOrderListVo {
    private String entrustNo;
    private String productTradeNo;
    private String productName;
    private BigDecimal price;
    private Integer entrustCount;
    private String customerNo;
    private String customerName;
    private String direction;
    private Byte anonymousFlag;
    private Date entrustTime;
    private Integer canRevokeCount;
    private Integer dealCount;
    private Integer waitCount;
}