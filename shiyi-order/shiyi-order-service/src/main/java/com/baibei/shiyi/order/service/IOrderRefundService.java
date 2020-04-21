package com.baibei.shiyi.order.service;

import com.baibei.shiyi.order.feign.base.vo.AdminOrderRefundVo;
import com.baibei.shiyi.order.model.OrderRefund;
import com.baibei.shiyi.common.core.mybatis.Service;

import java.util.List;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/07/31 18:16:01
 * @description: OrderRefund服务接口
 */
public interface IOrderRefundService extends Service<OrderRefund> {

    List<AdminOrderRefundVo> findByOrderId(Long orderId);

    List<OrderRefund> findByParam(Long orderItemId, String reviewStatus);

    /**
     * 查询子订单的退款的状态
     *
     * @param orderItemId
     * @param refundStatus
     * @return
     */
    List<OrderRefund> findByOrderItemIdByRefundStatus(Long orderItemId, String refundStatus);
}
