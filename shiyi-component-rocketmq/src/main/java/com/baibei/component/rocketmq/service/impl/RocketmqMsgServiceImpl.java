package com.baibei.component.rocketmq.service.impl;

import com.baibei.component.rocketmq.dao.RocketmqMsgMapper;
import com.baibei.component.rocketmq.model.RocketmqMsg;
import com.baibei.component.rocketmq.service.IRocketmqMsgService;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;


/**
* @author: 会跳舞的机器人
* @date: 2019/10/17 17:18:39
* @description: RocketmqMsg服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class RocketmqMsgServiceImpl extends AbstractService<RocketmqMsg> implements IRocketmqMsgService {

    @Autowired
    private RocketmqMsgMapper tblRocketmqMsgMapper;

}
