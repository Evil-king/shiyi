package com.baibei.shiyi.order.service;

import com.baibei.shiyi.order.model.PayChannel;
import com.baibei.shiyi.common.core.mybatis.Service;

import java.util.List;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/08/27 14:58:58
 * @description: PayChannel服务接口
 */
public interface IPayChannelService extends Service<PayChannel> {
    /**
     * 列表查询
     *
     * @param status
     * @return
     */
    List<PayChannel> list(String status);

    /**
     * 查询上架状态的
     *
     * @return
     */
    List<PayChannel> getUpPayChannelList();

    /**
     * 根据编码查询
     *
     * @param channelCode
     * @return
     */
    PayChannel findByCode(String channelCode);

}
