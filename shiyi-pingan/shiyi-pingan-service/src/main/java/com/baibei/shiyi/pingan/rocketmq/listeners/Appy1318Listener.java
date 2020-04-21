package com.baibei.shiyi.pingan.rocketmq.listeners;

import com.baibei.component.rocketmq.core.consumer.ConsumerProxyService;
import com.baibei.shiyi.pingan.rocketmq.comsume.Apply1318ConsumerImpl;
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
 * @description: 请求1318接口监听器
 */
@Component
@Slf4j
@RocketMQMessageListener(topic = "${rocketmq.withdraw.aplly1318.tx.topics}", consumerGroup = "${rocketmq.withdraw.aplly1318.tx.topics}" + "-consumerGroup")
public class Appy1318Listener implements RocketMQListener<MessageExt> {

    // 消费失败最大重试次数
    @Value("${rocketmq.maxReconsumeTimes}")
    private String maxReconsumeTimes;
    @Autowired
    private ConsumerProxyService consumerProxyService;
    @Autowired
    private Apply1318ConsumerImpl apply1318ConsumerImpl;

    @Override
    public void onMessage(MessageExt message) {
        log.info("监听到请求1318接口消息...",message);
        try {
            consumerProxyService.consume(apply1318ConsumerImpl, message);
        } catch (DuplicateKeyException e) { // 重复消费无需抛出异常进行重试
            log.info("repeat consume，message={}", message);
        } catch (Exception e) { // 其他异常向上抛出便于消费重试，RocketMQ已打印错误日志，此处无需打印
            throw e;
        }
    }
}