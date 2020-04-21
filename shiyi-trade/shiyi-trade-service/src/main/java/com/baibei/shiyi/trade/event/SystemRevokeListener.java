package com.baibei.shiyi.trade.event;

import com.alibaba.fastjson.JSON;
import com.baibei.shiyi.trade.biz.RevokeBiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author: 会跳舞的机器人
 * @date: 2020/1/10 15:51
 * @description: 闭市系统撤单事件监听器
 */
@Component
@Slf4j
public class SystemRevokeListener implements ApplicationListener<SystemRevokeEvent> {
    @Autowired
    private RevokeBiz revokeBiz;

    @Override
    @Async
    public void onApplicationEvent(SystemRevokeEvent systemRevokeEvent) {
        log.info("收到系统闭市撤单通知，systemRevokeEvent={}", JSON.toJSONString(systemRevokeEvent));
        try {
            // 休市60秒后撤单
            Thread.sleep(60000);
            revokeBiz.systemRevoke();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}