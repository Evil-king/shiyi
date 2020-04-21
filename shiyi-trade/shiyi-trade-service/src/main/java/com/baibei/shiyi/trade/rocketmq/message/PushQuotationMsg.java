package com.baibei.shiyi.trade.rocketmq.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: 会跳舞的机器人
 * @date: 2020/1/13 11:04
 * @description: 推送行情消息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PushQuotationMsg {
    private String operateType;
    private String productTradeNo;
    private BigDecimal price;
    private Integer count;
    private String orderNo;
    private Date occurTime;
}