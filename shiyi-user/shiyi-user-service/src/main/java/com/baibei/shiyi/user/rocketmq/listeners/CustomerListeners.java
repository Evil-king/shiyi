package com.baibei.shiyi.user.rocketmq.listeners;

import com.baibei.component.rocketmq.core.consumer.ConsumerProxyService;
import com.baibei.shiyi.user.rocketmq.comsume.UserCustomerImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RocketMQMessageListener(topic = "${rocketmq.customer.topics}", consumerGroup = "${rocketmq.customer.topics}--consumerGroup")
public class CustomerListeners implements RocketMQListener<MessageExt> {

    @Autowired
    private ConsumerProxyService consumerProxyService;

    @Autowired
    private UserCustomerImpl userCustomer;

    @Override
    public void onMessage(MessageExt message) {
        try {
            consumerProxyService.consume(userCustomer, message);
        } catch (DuplicateKeyException ex) {
            log.info("repeat consume，message={}", message);
        } catch (Exception ex) {
            throw ex;
        }
    }
}
