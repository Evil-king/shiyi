package com.baibei.shiyi.order.rocketmq.listener;

import com.baibei.component.rocketmq.core.consumer.ConsumerProxyService;
import com.baibei.shiyi.order.rocketmq.comsumer.DemoConsumerImpl;
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
 * @date: 2019/10/9 19:44
 * @description: RocketMQ监听样例
 */
@Component
@Slf4j
// notice：如果指定了tags，consumerGroup的值应该为：（topics名称）+（tags名称）+（-consumerGroup）
@RocketMQMessageListener(topic = "Topic-Demo", consumerGroup = "Topic-Demo-consumerGroup")
public class DemoListener implements RocketMQListener<MessageExt> {
    // 消费失败最大重试次数
    @Value("${rocketmq.maxReconsumeTimes}")
    private String maxReconsumeTimes;
    @Autowired
    private ConsumerProxyService consumerProxyService;
    @Autowired
    private DemoConsumerImpl demoConsumer;

    @Override
    public void onMessage(MessageExt message) {
        try {
            consumerProxyService.consume(demoConsumer, message);
        } catch (DuplicateKeyException e) { // 重复消费无需抛出异常进行重试
            log.info("repeat consume，message={}", message);
        } catch (Exception e) { // 其他异常向上抛出便于消费重试，RocketMQ已打印错误日志，此处无需打印
            throw e;
        }
    }
}