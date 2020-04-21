package com.baibei.shiyi.order.rocketmq.listener;

import com.baibei.component.rocketmq.core.consumer.ConsumerProxyService;
import com.baibei.shiyi.order.rocketmq.comsumer.OrderPayConsumerImpl;
import com.baibei.shiyi.order.rocketmq.comsumer.OrderReportConsumerImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

/**
 * @author: Longer
 * @date: 2019/12/04 19:39
 * @description: 请求福清，确认支付
 */
@Component
@Slf4j
@RocketMQMessageListener(topic = "${rocketmq.orderPay.topics}", consumerGroup = "${rocketmq.orderPay.topics}" + "-consumerGroup")
public class OrderPayListener implements RocketMQListener<MessageExt> {

    @Autowired
    private ConsumerProxyService consumerProxyService;
    @Autowired
    private OrderPayConsumerImpl orderPayConsumer;

    @Override
    public void onMessage(MessageExt message) {
        log.info("OrderPayListener 监听到消息：{}",message);
        try {
            consumerProxyService.consume(orderPayConsumer, message);
        } catch (DuplicateKeyException e) { // 重复消费无需抛出异常进行重试
            log.info("repeat consume，message={}", message);
        } catch (Exception e) { // 其他异常向上抛出便于消费重试，RocketMQ已打印错误日志，此处无需打印
            throw e;
        }
    }
}