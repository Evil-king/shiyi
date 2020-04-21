package com.baibei.shiyi.order.rocketmq.listener;

import com.baibei.component.rocketmq.core.consumer.ConsumerProxyService;
import com.baibei.shiyi.order.rocketmq.comsumer.CancelOrderConsumerImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/10/11 15:03
 * @description: 未付款订单超时自动取消消息监听
 */
@Component
@Slf4j
@RocketMQMessageListener(topic = "${rocketmq.cancelOrder.topics}", consumerGroup = "${rocketmq.cancelOrder.topics}" + "-consumerGroup")
public class CancelOrderListener implements RocketMQListener<MessageExt> {

    // 消费失败最大重试次数
    @Value("${rocketmq.maxReconsumeTimes}")
    private String maxReconsumeTimes;
    @Autowired
    private ConsumerProxyService consumerProxyService;
    @Autowired
    private CancelOrderConsumerImpl cancelOrderConsumer;

    @Override
    public void onMessage(MessageExt message) {
        try {
            consumerProxyService.consume(cancelOrderConsumer, message);
        } catch (DuplicateKeyException e) { // 重复消费无需抛出异常进行重试
            log.info("repeat consume，message={}", message);
        } catch (Exception e) { // 其他异常向上抛出便于消费重试，RocketMQ已打印错误日志，此处无需打印
            throw e;
        }
    }
}