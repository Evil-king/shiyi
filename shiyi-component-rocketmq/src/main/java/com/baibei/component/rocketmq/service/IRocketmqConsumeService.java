package com.baibei.component.rocketmq.service;

import com.baibei.component.rocketmq.model.RocketmqConsume;
import com.baibei.shiyi.common.core.mybatis.Service;
import org.apache.rocketmq.common.message.MessageExt;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/10/17 17:17:35
 * @description: RocketmqConsume服务接口
 */
public interface IRocketmqConsumeService extends Service<RocketmqConsume> {

    /**
     * 保存消息消费信息
     *
     * @param messageExt
     * @param consumeStatus
     */
    void save(MessageExt messageExt, String consumeStatus);

}
