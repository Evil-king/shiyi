package com.baibei.shiyi.trade.rocketmq.listeners;

import com.baibei.component.rocketmq.core.consumer.ConsumerProxyService;
import com.baibei.shiyi.trade.rocketmq.comsumers.PushQuotationConsumerImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/12/25 10:27
 * @description: 推送行情消息监听
 */

@Component
@Slf4j
@RocketMQMessageListener(topic = "${rocketmq.trade.pushQuotation.topics}", consumerGroup = "${rocketmq.trade.pushQuotation.topics}" + "-consumerGroup")
public class PushQuotationListener implements RocketMQListener<MessageExt> {
    @Autowired
    private ConsumerProxyService consumerProxyService;
    @Autowired
    private PushQuotationConsumerImpl pushQuotationConsumer;


    @Override
    public void onMessage(MessageExt message) {
        try {
            consumerProxyService.consume(pushQuotationConsumer, message);
        } catch (DuplicateKeyException e) { // 重复消费无需抛出异常进行重试
            log.info("repeat consume，message={}", message);
        } catch (Exception e) { // 其他异常向上抛出便于消费重试，RocketMQ已打印错误日志，此处无需打印
            throw e;
        }
    }
}
