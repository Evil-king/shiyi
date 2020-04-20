package com.baibei.shiyi.cash.feign.base.message;

import com.baibei.shiyi.cash.feign.base.dto.CashChangeAmountDto;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Classname DealDiffMessage
 * @Description 调账修改资金消息体
 * @Date 2019/11/6 17:22
 * @Created by Longer
 */
@Data
public class DealDiffMessage {

    /**
     * 差异id
     */
    private Long diffId;

    /**
     * 是否需要变动资金标识(true=需要；false=不需要)。用于判断是否需要发送异步消息操作用户资金
     */
    private boolean changeFlag = false;

    /**
     * 订单类型(deposit=入金；withdraw=入金)
     */
    private String orderType;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 出入金订单状态(扣款后需要将订单修改成该指定状态)
     */
    private String orderStatus;

    /**
     * 差异类型
     */
    private String diffType;

    /**
     * 银行端流水的出入金金额（如果是金额不一致或者是状态和金额都不一致的情况下，需要将系统订单的金额更新为该金额）
     */
    private BigDecimal bankAmount;

    /**
     * 出金：
     * 银行端流水手续费（如果是金额不一致或者是状态和金额都不一致的情况下，需要将系统订单的手续费更新为该金额）
     * 注：入金无手续费
     */
    private BigDecimal bankFee;

    private List<CashChangeAmountDto> cashChangeAmountDtoList = new ArrayList<>();

    /**
     * 异步修改资金结果标识（success=成功；fail=失败）
     */
    private String resultFlag;
}
