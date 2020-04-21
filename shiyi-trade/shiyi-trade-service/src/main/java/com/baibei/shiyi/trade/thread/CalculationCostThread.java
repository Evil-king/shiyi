package com.baibei.shiyi.trade.thread;

import com.baibei.shiyi.trade.service.IHoldDetailsService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: 会跳舞的机器人
 * @date: 2020/1/8 13:40
 * @description: 计算持仓成本价
 */
@Slf4j
public class CalculationCostThread implements Runnable {
    private String customerNo;
    private String productTradeNo;
    private IHoldDetailsService holdDetailsService;

    public CalculationCostThread(String customerNo, String productTradeNo, IHoldDetailsService holdDetailsService) {
        this.customerNo = customerNo;
        this.productTradeNo = productTradeNo;
        this.holdDetailsService = holdDetailsService;
    }

    @Override
    public void run() {
        log.info("异步更新持仓成本，CalculationCostThread=【{}】", this.toString());
        holdDetailsService.calculationCost(customerNo, productTradeNo);
    }

    @Override
    public String toString() {
        return "CalculationCostThread{" +
                "customerNo='" + customerNo + '\'' +
                ", productTradeNo='" + productTradeNo + '\'' +
                ", holdDetailsService=" + holdDetailsService +
                '}';
    }
}