package com.baibei.shiyi.pingan.rocketmq;

import com.baibei.component.rocketmq.core.util.RocketMQUtil;
import com.baibei.shiyi.common.tool.api.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/8/8 18:10
 * @description:
 */
@RestController
@RequestMapping("/api/rocketmq")
@Slf4j
public class RocketMQDemoController {
    @Autowired
    private RocketMQUtil rocketMQUtil;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 发送消息
     *
     * @return
     */
    @GetMapping("/sendMsg")
    public ApiResult sendMsg() {
        testTransaction2();
        return ApiResult.success();
    }

    private void testTransaction() throws MessagingException {
        String[] tags = new String[]{"TagA", "TagB", "TagC", "TagD", "TagE"};
        for (int i = 0; i < 10; i++) {
            try {
                Message msg = MessageBuilder.withPayload("Hello RocketMQ " + i).
                        setHeader(RocketMQHeaders.TRANSACTION_ID, "KEY_" + i).build();
                SendResult sendResult = rocketMQTemplate.sendMessageInTransaction("myTxProducerGroup",
                        "spring-transaction-topic" + ":" + tags[i % tags.length], msg, null);
                System.out.printf("------ send Transactional msg body = %s , sendResult=%s %n",
                        msg.getPayload(), sendResult.getSendStatus());
                Thread.sleep(10);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void testTransaction2() throws MessagingException {
        String transaction_id = UUID.randomUUID().toString();
        Message msg = MessageBuilder.withPayload("Hello RocketMQ ").
                setHeader(RocketMQHeaders.TRANSACTION_ID, transaction_id).build();
        SendResult sendResult = rocketMQTemplate.sendMessageInTransaction("myTxProducerGroup",
                "spring-transaction-topic", msg, null);
        System.out.printf("------ send Transactional msg body = %s , sendResult=%s %n",
                msg.getPayload(), sendResult.getSendStatus());
    }
}