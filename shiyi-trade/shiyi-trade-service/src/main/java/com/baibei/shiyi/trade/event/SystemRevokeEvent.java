package com.baibei.shiyi.trade.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author: 会跳舞的机器人
 * @date: 2020/1/10 15:49
 * @description: 系统撤单事件
 */
public class SystemRevokeEvent extends ApplicationEvent {
    private String tradeDate;

    public SystemRevokeEvent(Object source, String tradeDate) {
        super(source);
        this.tradeDate = tradeDate;
    }

    public String getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate;
    }
}