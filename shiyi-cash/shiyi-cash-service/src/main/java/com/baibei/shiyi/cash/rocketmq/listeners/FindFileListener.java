package com.baibei.shiyi.cash.rocketmq.listeners;

import com.baibei.component.rocketmq.core.consumer.ConsumerProxyService;
import com.baibei.shiyi.cash.rocketmq.comsume.Apply1318AckConsumerImpl;
import com.baibei.shiyi.cash.rocketmq.comsume.FindFileConsumerImpl;
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
 * @description: 查看文件消息监听
 */
@Component
@Slf4j
@RocketMQMessageListener(topic = "${rocketmq.findFile.topic}",selectorExpression = "cashTag", consumerGroup = "${rocketmq.findFile.topic}"+"-cashTag" + "-consumerGroup")
public class FindFileListener implements RocketMQListener<MessageExt> {

    @Autowired
    private ConsumerProxyService consumerProxyService;
    @Autowired
    private FindFileConsumerImpl findFileComsumer;

    @Override
    public void onMessage(MessageExt message) {
        log.info("FindFileListener is running....");
        try {
            consumerProxyService.consume(findFileComsumer, message);
        } catch (DuplicateKeyException e) { // 重复消费无需抛出异常进行重试
            log.info("repeat consume，message={}", message);
        } catch (Exception e) { // 其他异常向上抛出便于消费重试，RocketMQ已打印错误日志，此处无需打印
            throw e;
        }
    }
}