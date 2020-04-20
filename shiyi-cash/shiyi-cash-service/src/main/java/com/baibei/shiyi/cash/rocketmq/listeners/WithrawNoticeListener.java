package com.baibei.shiyi.cash.rocketmq.listeners;

import com.baibei.component.rocketmq.core.consumer.ConsumerProxyService;
import com.baibei.shiyi.cash.rocketmq.comsume.Apply1010ConsumerImpl;
import com.baibei.shiyi.cash.rocketmq.comsume.WithdrawNoticeConsumerImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

/**
 * @author: Longer
 * @date: 2019/12/04 14:13
 * @description: 出金异步结果通知
 */
@Component
@Slf4j
@RocketMQMessageListener(topic = "Topic-withrawNotice", consumerGroup = "Topic-withrawNotice"+ "-consumerGroup")
public class WithrawNoticeListener implements RocketMQListener<MessageExt> {

    @Autowired
    private ConsumerProxyService consumerProxyService;
    @Autowired
    private WithdrawNoticeConsumerImpl withrawNoticeConsumer;

    @Override
    public void onMessage(MessageExt message) {
        log.info("WithrawNoticeListener is running....，message={}",message);
        try {
            consumerProxyService.consume(withrawNoticeConsumer, message);
        } catch (DuplicateKeyException e) { // 重复消费无需抛出异常进行重试
            log.info("repeat consume，message={}", message);
        } catch (Exception e) { // 其他异常向上抛出便于消费重试，RocketMQ已打印错误日志，此处无需打印
            throw e;
        }
    }
}