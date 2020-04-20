package com.baibei.shiyi.cash.rocketmq.listeners;

import com.baibei.component.rocketmq.core.consumer.ConsumerProxyService;
import com.baibei.shiyi.cash.rocketmq.comsume.Apply1010ConsumerImpl;
import com.baibei.shiyi.cash.rocketmq.comsume.FindFileConsumerImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

/**
 * @author: Longer
 * @date: 2019/11/1 10:41
 * @description: 台账消息监听
 */
@Component
@Slf4j
@RocketMQMessageListener(topic = "${rocketmq.apply1010.topics}", consumerGroup = "${rocketmq.apply1010.topics}"+ "-consumerGroup")
public class Apply1010Listener implements RocketMQListener<MessageExt> {

    @Autowired
    private ConsumerProxyService consumerProxyService;
    @Autowired
    private Apply1010ConsumerImpl apply1010Consumer;

    @Override
    public void onMessage(MessageExt message) {
        log.info("Apply1010Listener is running....");
        try {
            consumerProxyService.consume(apply1010Consumer, message);
        } catch (DuplicateKeyException e) { // 重复消费无需抛出异常进行重试
            log.info("repeat consume，message={}", message);
        } catch (Exception e) { // 其他异常向上抛出便于消费重试，RocketMQ已打印错误日志，此处无需打印
            throw e;
        }
    }
}