package com.baibei.shiyi.account.rocketmq.listeners;

import com.baibei.component.rocketmq.core.consumer.ConsumerProxyService;
import com.baibei.shiyi.account.rocketmq.comsume.WithdrawAddMoneyConsumerImpl;
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
 * @description: 审核出金，审核不通过，则需要加回用户的钱
 */
@Component
@Slf4j
@RocketMQMessageListener(topic = "${rocketmq.withdraw.addmoney.topics}", consumerGroup = "${rocketmq.withdraw.addmoney.topics}" + "-consumerGroup")
public class WithdrawAddMoneyListener implements RocketMQListener<MessageExt> {

    // 消费失败最大重试次数
//    @Value("${rocketmq.maxReconsumeTimes}")
    @Value("1000000")
    private String maxReconsumeTimes;
    @Autowired
    private ConsumerProxyService consumerProxyService;
    @Autowired
    private WithdrawAddMoneyConsumerImpl withdrawAddMoneyConsumer;

    @Override
    public void onMessage(MessageExt message) {
        try {
            consumerProxyService.consume(withdrawAddMoneyConsumer, message);
        } catch (DuplicateKeyException e) { // 重复消费无需抛出异常进行重试
            log.info("repeat consume，message={}", message);
        } catch (Exception e) { // 其他异常向上抛出便于消费重试，RocketMQ已打印错误日志，此处无需打印
            throw e;
        }
    }
}