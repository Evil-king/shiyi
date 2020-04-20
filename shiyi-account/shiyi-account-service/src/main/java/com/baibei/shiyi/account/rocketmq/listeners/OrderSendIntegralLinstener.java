package com.baibei.shiyi.account.rocketmq.listeners;

import com.baibei.component.rocketmq.core.consumer.ConsumerProxyService;
import com.baibei.shiyi.account.rocketmq.comsume.OrderSendIntegralConsumerImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/11/6 14:13
 * @description:
 */
@Component
@Slf4j
@RocketMQMessageListener(topic = "${rocketmq.order.sendIntegral.topics}", consumerGroup = "${rocketmq.order.sendIntegral.topics}" + "-consumerGroup")
public class OrderSendIntegralLinstener implements RocketMQListener<MessageExt> {
    @Autowired
    private ConsumerProxyService consumerProxyService;
    @Autowired
    private OrderSendIntegralConsumerImpl orderSendIntegralConsumer;

    @Override
    public void onMessage(MessageExt message) {
        try {
            consumerProxyService.consume(orderSendIntegralConsumer, message);
        } catch (DuplicateKeyException e) { // 重复消费无需抛出异常进行重试
            log.info("repeat consume，message={}", message);
        } catch (Exception e) { // 其他异常向上抛出便于消费重试，RocketMQ已打印错误日志，此处无需打印
            throw e;
        }
    }
}