package com.baibei.shiyi.cash.rocketmq.listeners;

import com.baibei.component.rocketmq.core.consumer.ConsumerProxyService;
import com.baibei.shiyi.cash.rocketmq.comsume.WithdrawDetuchAccountAckConsumerImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

/**
 * @author: Longer
 * @date: 2019/11/1 10:41
 * @description: 用户出金申请，确认扣钱消息监听
 */
@Component
@Slf4j
@RocketMQMessageListener(topic = "${rocketmq.withdrawDetuchAccountAck.topics}", consumerGroup = "${rocketmq.withdrawDetuchAccountAck.topics}" + "-consumerGroup")
public class WithdrawDetuchAccountAckListener implements RocketMQListener<MessageExt> {

    // 消费失败最大重试次数
    @Value("${rocketmq.maxReconsumeTimes}")
    private String maxReconsumeTimes;
    @Autowired
    private ConsumerProxyService consumerProxyService;
    @Autowired
    private WithdrawDetuchAccountAckConsumerImpl withdrawDetuchAccountAckConsumer;

    @Override
    public void onMessage(MessageExt message) {
        try {
            consumerProxyService.consume(withdrawDetuchAccountAckConsumer, message);
        } catch (DuplicateKeyException e) { // 重复消费无需抛出异常进行重试
            log.info("repeat consume，message={}", message);
        } catch (Exception e) { // 其他异常向上抛出便于消费重试，RocketMQ已打印错误日志，此处无需打印
            throw e;
        }
    }
}