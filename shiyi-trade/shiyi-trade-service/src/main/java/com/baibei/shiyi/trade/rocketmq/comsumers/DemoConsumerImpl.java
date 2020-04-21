package com.baibei.shiyi.trade.rocketmq.comsumers;

import com.alibaba.fastjson.JSON;
import com.baibei.component.rocketmq.core.consumer.IConsumer;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.trade.rocketmq.message.DemoMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/10/10 13:38
 * @description: 具体的消费逻辑类
 */
@Component
@Slf4j
public class DemoConsumerImpl implements IConsumer<DemoMsg> {

    public ApiResult execute(DemoMsg demoMsg) {
        log.info("DemoConsumerImpl execute,msg={}", JSON.toJSONString(demoMsg));
        // 业务逻辑实现

        return ApiResult.success();
    }
}