package com.baibei.shiyi.order.dao;

import com.baibei.shiyi.common.core.mybatis.MyMapper;
import com.baibei.shiyi.order.feign.base.vo.AdminOrderRefundVo;
import com.baibei.shiyi.order.model.OrderRefund;

import java.util.List;

public interface OrderRefundMapper extends MyMapper<OrderRefund> {

    List<AdminOrderRefundVo> findByOrderId(Long orderId);
}