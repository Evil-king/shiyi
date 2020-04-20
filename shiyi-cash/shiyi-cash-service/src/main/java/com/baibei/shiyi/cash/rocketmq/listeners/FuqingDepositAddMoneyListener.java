package com.baibei.shiyi.cash.rocketmq.listeners;

import com.baibei.component.rocketmq.core.consumer.ConsumerProxyService;
import com.baibei.shiyi.cash.rocketmq.comsume.FuqingDepositAddMoneyConsumerImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RocketMQMessageListener(topic = "${rocketmq.fuqingDeposit.topics}", consumerGroup = "${rocketmq.fuqingDeposit.topics}" + "-consumerGroup")
public class FuqingDepositAddMoneyListener implements RocketMQListener<MessageExt> {

    @Autowired
    private FuqingDepositAddMoneyConsumerImpl fuqingDepositAddMoneyConsumer;

    @Autowired
    private ConsumerProxyService consumerProxyService;

    @Override
    public void onMessage(MessageExt messageExt) {
        try {
            consumerProxyService.consume(fuqingDepositAddMoneyConsumer, messageExt);
        } catch (DuplicateKeyException e) { // 重复消费无需抛出异常进行重试
            log.info("repeat consume，message={}", messageExt);
        } catch (Exception e) { // 其他异常向上抛出便于消费重试，RocketMQ已打印错误日志，此处无需打印
            throw e;
        }
    }
}
