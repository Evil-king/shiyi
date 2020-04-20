package com.baibei.shiyi.account.rocketmq.listeners;


import com.baibei.component.rocketmq.core.consumer.ConsumerProxyService;
import com.baibei.shiyi.account.rocketmq.comsume.DepositAddMoneyConsumerImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

/***
 * 入金的消息监听发送
 */
@Component
@Slf4j
@RocketMQMessageListener(topic = "${rocketmq.deposit.topics}", consumerGroup = "${rocketmq.deposit.topics}" + "-consumerGroup")
public class DepositAddMoneyListener implements RocketMQListener<MessageExt> {

    // 消费失败最大重试次数
    @Value("${rocketmq.maxReconsumeTimes}")
    private String maxReconsumeTimes;
    // 消费的代理
    @Autowired
    private ConsumerProxyService consumerProxyService;

    @Autowired
    private DepositAddMoneyConsumerImpl depositAddMoneyConsumer;

    @Override
    public void onMessage(MessageExt message) {
        try {
            consumerProxyService.consume(depositAddMoneyConsumer, message);
        } catch (DuplicateKeyException e) { // 重复消费无需抛出异常进行重试
            log.info("repeat consume，message={}", message);
        } catch (Exception e) { // 其他异常向上抛出便于消费重试，RocketMQ已打印错误日志，此处无需打印
            throw e;
        }
    }
}
