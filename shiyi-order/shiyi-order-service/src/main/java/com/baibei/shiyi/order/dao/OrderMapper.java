package com.baibei.shiyi.order.dao;

import com.baibei.shiyi.common.core.mybatis.MyMapper;
import com.baibei.shiyi.order.common.vo.MyOrderTempVo;
import com.baibei.shiyi.order.feign.base.dto.AdminOrderDto;
import com.baibei.shiyi.order.feign.base.vo.AdminOrderVo;
import com.baibei.shiyi.order.model.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderMapper extends MyMapper<Order> {
    List<MyOrderTempVo> unionAllList(@Param("customerNo") String customerNo, @Param("orderStatus") String orderStatus,
                                     @Param("orderItemStatus") String orderItemStatus);

    List<AdminOrderVo> orderPageList(AdminOrderDto adminOrderDto);

    List<AdminOrderVo> findByOrderList(AdminOrderDto adminOrderDto);

}