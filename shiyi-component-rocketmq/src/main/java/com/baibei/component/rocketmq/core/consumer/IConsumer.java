package com.baibei.component.rocketmq.core.consumer;

import com.baibei.shiyi.common.tool.api.ApiResult;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/10/10 11:46
 * @description: 消费者接口，其他消费者需实现该接口
 */
public interface IConsumer<T> {

    /**
     * 执行具体的消费逻辑
     *
     * @param t
     * @return
     */
    ApiResult execute(T t);

}