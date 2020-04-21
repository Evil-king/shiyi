package com.baibei.shiyi.order.rocketmq.comsumer;

import com.baibei.component.rocketmq.core.consumer.IConsumer;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.order.biz.CancelOrderBiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/10/11 14:53
 * @description: 未付款订单超时自动取消消息消费
 */
@Component
@Slf4j
public class CancelOrderConsumerImpl implements IConsumer<String> {
    @Autowired
    private CancelOrderBiz cancelOrderBiz;

    @Override
    public ApiResult execute(String s) {
        return cancelOrderBiz.cancel(s, "系统自动取消");
    }
}