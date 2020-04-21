package com.baibei.shiyi.order.dao;

import com.baibei.shiyi.common.core.mybatis.MyMapper;
import com.baibei.shiyi.order.feign.base.vo.AdminOrderProductVo;
import com.baibei.shiyi.order.model.OrderProduct;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderProductMapper extends MyMapper<OrderProduct> {

    List<AdminOrderProductVo> findAdminByOrderId(@Param(value = "orderId") Long orderId);
}