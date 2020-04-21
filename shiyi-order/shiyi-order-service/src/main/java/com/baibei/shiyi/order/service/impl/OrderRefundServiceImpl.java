package com.baibei.shiyi.order.service.impl;

import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.order.dao.OrderRefundMapper;
import com.baibei.shiyi.order.feign.base.vo.AdminOrderRefundVo;
import com.baibei.shiyi.order.model.OrderRefund;
import com.baibei.shiyi.order.service.IOrderRefundService;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/07/31 18:16:01
 * @description: OrderRefund服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class OrderRefundServiceImpl extends AbstractService<OrderRefund> implements IOrderRefundService {

    @Autowired
    private OrderRefundMapper tblOrdOrderRefundMapper;


    @Override
    public List<AdminOrderRefundVo> findByOrderId(Long orderId) {
        List<AdminOrderRefundVo> orderRefundVos = tblOrdOrderRefundMapper.findByOrderId(orderId);
        orderRefundVos.stream().forEach(x -> {
                    if (x.getRefundStatus().equals(Constants.RefundStatus.APPLY_REFUND)) {
                        x.setRefundStatusText("退款处理中");
                    }
                    if (x.getRefundStatus().equals(Constants.RefundStatus.REFUNDED)) {
                        x.setRefundStatusText("退款成功");
                    }
                    if (x.getRefundStatus().equals(Constants.RefundStatus.REJECT_REFUND)) {
                        x.setRefundStatusText("退款拒绝");
                    }
                }
        );
        return orderRefundVos;
    }

    @Override
    public List<OrderRefund> findByParam(Long orderItemId, String reviewStatus) {
        Condition condition = new Condition(OrderRefund.class);
        buildValidCriteria(condition).andEqualTo("orderItemId", orderItemId)
                .andEqualTo("reviewStatus", reviewStatus);
        return findByCondition(condition);
    }

    @Override
    public List<OrderRefund> findByOrderItemIdByRefundStatus(Long orderItemId, String refundStatus) {
        Condition condition = new Condition(OrderRefund.class);
        buildValidCriteria(condition).andEqualTo("orderItemId", orderItemId)
                .andEqualTo("refundStatus", refundStatus);
        return findByCondition(condition);
    }
}
