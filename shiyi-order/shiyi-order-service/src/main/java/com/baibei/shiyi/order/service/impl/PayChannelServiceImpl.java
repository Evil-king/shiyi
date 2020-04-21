package com.baibei.shiyi.order.service.impl;

import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.order.dao.PayChannelMapper;
import com.baibei.shiyi.order.model.PayChannel;
import com.baibei.shiyi.order.service.IPayChannelService;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.List;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/08/27 14:58:58
 * @description: PayChannel服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PayChannelServiceImpl extends AbstractService<PayChannel> implements IPayChannelService {

    @Autowired
    private PayChannelMapper tblOrdPayChannelMapper;

    @Override
    public List<PayChannel> list(String status) {
        Condition condition = new Condition(PayChannel.class);
        Example.Criteria criteria = buildValidCriteria(condition);
        if (!StringUtils.isEmpty(status)) {
            criteria.andEqualTo("status", status);
        }
        return findByCondition(condition);
    }

    @Override
    public List<PayChannel> getUpPayChannelList() {
        return list(Constants.UpDown.UP);
    }

    @Override
    public PayChannel findByCode(String channelCode) {
        if (StringUtils.isEmpty(channelCode)) {
            throw new IllegalArgumentException("channelCode can not be null");
        }
        Condition condition = new Condition(PayChannel.class);
        Example.Criteria criteria = buildValidCriteria(condition);
        criteria.andEqualTo("channelCode", channelCode);
        List<PayChannel> list = findByCondition(condition);
        return CollectionUtils.isEmpty(list) ? null : list.get(0);
    }
}
