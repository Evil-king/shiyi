package com.baibei.shiyi.order.rocketmq.comsumer;

import com.baibei.component.rocketmq.core.consumer.IConsumer;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.order.biz.SplitOrderBiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/10/11 14:59
 * @description: 拆单消息消费
 */
@Component
@Slf4j
public class SplitOrderConsumerImpl implements IConsumer<String> {
    @Autowired
    private SplitOrderBiz splitOrderBiz;

    @Override
    public ApiResult execute(String s) {
        return splitOrderBiz.splitOrder(s);
    }
}