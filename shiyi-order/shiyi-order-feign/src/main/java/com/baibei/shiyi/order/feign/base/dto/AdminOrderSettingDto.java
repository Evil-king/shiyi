package com.baibei.shiyi.order.feign.base.dto;

import lombok.Data;

@Data
public class AdminOrderSettingDto {


    /**
     * 秒杀订单超过()分
     */
    private Integer flashOrderOvertime;

    /**
     * 正常订单超过()分
     */
    private Integer normalOrderOvertime;


    /**
     * 发货超过()天
     */
    private Integer confirmOvertime;

    /**
     * 发货完成多少天
     */
    private Integer finishOvertime;
}
