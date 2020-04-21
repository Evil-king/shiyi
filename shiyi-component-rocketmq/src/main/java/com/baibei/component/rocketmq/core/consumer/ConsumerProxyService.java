package com.baibei.component.rocketmq.core.consumer;

import com.baibei.component.rocketmq.core.util.RocketMQUtil;
import com.baibei.component.rocketmq.service.IRocketmqConsumeService;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.exception.ServiceException;
import com.baibei.shiyi.common.tool.utils.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/10/10 11:43
 * @description: 消费代理者，统一实现消费幂等
 */
@Component
@Slf4j
@Transactional(rollbackFor = Exception.class)   // 在此类中控制事务即可
public class ConsumerProxyService {
    @Autowired
    public IRocketmqConsumeService rocketmqConsumeService;


    /**
     * 消费消息
     *
     * @param consumer
     * @param messageExt
     * @return·
     */
    public ApiResult consume(IConsumer consumer, MessageExt messageExt) {
        long start = System.currentTimeMillis();
        String consumerName = consumer.getClass().getSimpleName();
        log.info("begin consume，consumer is {}，messageExt={}", consumerName, messageExt.toString());
        if (consumer == null) {
            throw new IllegalArgumentException("consumerTemplate cannot be null");
        }
        if (messageExt == null) {
            throw new IllegalArgumentException("messageExt cannot be null");
        }
        // 获取消息体
        String body = RocketMQUtil.getStrFromByte(messageExt.getBody());
        if (StringUtils.isEmpty(body)) {
            log.info("message body is empty");
            throw new IllegalArgumentException("message body is empty");
        }
        log.info("message body is：{}", body);
        Object object = null;
        // 获取接口上的泛型类型
        ResolvableType resolvableType1 = ResolvableType.forClass(consumer.getClass());
        Class<?> clazz = resolvableType1.getInterfaces()[0].getGenerics()[0].resolve();
        // List需要获取集合上的泛型
        if ("List".equals(clazz.getSimpleName())) {
            Class<?> clazz2 = resolvableType1.getInterfaces()[0].getGenerics()[0].getGeneric(0).resolve();
            object = JacksonUtil.jsonToBeanList(body, clazz2);
        } else {
            object = JacksonUtil.jsonToBean(body, clazz);
        }
        // 执行具体的消费逻辑
        ApiResult apiResult = consumer.execute(object);
        if (!apiResult.hasSuccess()) {
            log.info("consume fail，apiResult={}", apiResult.toString());
            throw new ServiceException(apiResult.getMsg());
        }
        // 写入消息消费表
        try {
            rocketmqConsumeService.save(messageExt, RocketMQUtil.SUCCESS);
        } catch (DuplicateKeyException e) { // 此处抛出异常便于事务回滚
            throw e;
        }
        log.info("end consume，consumer is {}，timeconsuming is {} ms，messageExt={}", consumerName,
                (System.currentTimeMillis() - start), messageExt.toString());
        return apiResult;
    }
}