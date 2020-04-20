package com.baibei.component.rocketmq.service.impl;

import com.baibei.component.rocketmq.core.util.RocketMQUtil;
import com.baibei.component.rocketmq.dao.RocketmqConsumeMapper;
import com.baibei.component.rocketmq.model.RocketmqConsume;
import com.baibei.component.rocketmq.service.IRocketmqConsumeService;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import com.baibei.shiyi.common.tool.utils.IdWorker;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/10/17 17:17:35
 * @description: RocketmqConsume服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RocketmqConsumeServiceImpl extends AbstractService<RocketmqConsume> implements IRocketmqConsumeService {

    @Autowired
    private RocketmqConsumeMapper tblRocketmqConsumeMapper;

    @Override
    public void save(MessageExt messageExt, String consumeStatus) {
        RocketmqConsume rocketmqConsume = new RocketmqConsume();
        rocketmqConsume.setId(IdWorker.getId());
        rocketmqConsume.setTopic(messageExt.getTopic());
        rocketmqConsume.setTags(StringUtils.isEmpty(messageExt.getTags()) ? "*" : messageExt.getTags());
        rocketmqConsume.setMessageContent(RocketMQUtil.getStrFromByte(messageExt.getBody()));
        rocketmqConsume.setMessageExt(messageExt.toString());
        rocketmqConsume.setMessageKey(messageExt.getKeys());
        rocketmqConsume.setConsumeStatus(consumeStatus);
        rocketmqConsume.setCreateTime(new Date());
        if (!StringUtils.isEmpty(consumeStatus) && RocketMQUtil.FAIL.equals(consumeStatus)) {
            rocketmqConsume.setProcessStatus(RocketMQUtil.WAIT);
        }
        this.save(rocketmqConsume);
    }
}
