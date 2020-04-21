package com.baibei.shiyi.order.dao;

import com.baibei.shiyi.common.core.mybatis.MyMapper;
import com.baibei.shiyi.order.feign.base.dto.AdminOrderDto;
import com.baibei.shiyi.order.feign.base.vo.AdminOrderItemVo;
import com.baibei.shiyi.order.model.OrderItem;

import java.util.List;
import java.util.Map;

public interface OrderItemMapper extends MyMapper<OrderItem> {

    List<OrderItem> findConfirmOverTimeOrder(Map<String, Object> params);

    List<AdminOrderItemVo> orderAndOrderItemPageList(AdminOrderDto adminOrderDto);
}