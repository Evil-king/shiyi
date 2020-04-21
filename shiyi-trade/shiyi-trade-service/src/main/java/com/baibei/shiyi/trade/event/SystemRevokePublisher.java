package com.baibei.shiyi.trade.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author: 会跳舞的机器人
 * @date: 2020/1/10 15:51
 * @description: 发布闭市系统撤单
 */
@Slf4j
@Component
public class SystemRevokePublisher {
    @Autowired
    private ApplicationContext applicationContext;

    public void push(String tradeDate) {
        applicationContext.publishEvent(new SystemRevokeEvent(this, tradeDate));
    }
}