package com.baibei.shiyi.trade.service;

import com.baibei.shiyi.trade.model.EntrustOrder;

/**
 * @author: 会跳舞的机器人
 * @date: 2020/1/8 17:24
 * @description:
 */
public interface IRevokeService {
    /**
     * 撤挂买单
     *
     * @param entrustOrder
     * @param result
     */
    void revokeBuyOrder(EntrustOrder entrustOrder, String result);

    /**
     * 撤挂卖单
     *
     * @param entrustOrder
     * @param result
     */
    void revokeSellOrder(EntrustOrder entrustOrder, String result);
}