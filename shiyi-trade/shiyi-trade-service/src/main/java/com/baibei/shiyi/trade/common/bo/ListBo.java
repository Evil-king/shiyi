package com.baibei.shiyi.trade.common.bo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/12/26 15:32
 * @description: 挂牌业务对象
 */
@Data
@Builder
public class ListBo {
    private String entrustNo;
    private String customerNo;
    private String customerName;
    private String productTradeNo;
    private String direction;
    private BigDecimal price;
    private Integer count;
    private Byte anonymousFlag;
    private String status;
    private BigDecimal changeAmount;
    private BigDecimal buyFee;
    private Date entrustTime;
}

