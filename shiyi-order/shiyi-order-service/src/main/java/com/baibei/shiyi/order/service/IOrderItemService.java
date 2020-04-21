package com.baibei.shiyi.order.service;

import com.baibei.shiyi.common.core.mybatis.Service;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.order.common.dto.MyOrderDto;
import com.baibei.shiyi.order.common.vo.MyOrderVo;
import com.baibei.shiyi.order.feign.base.dto.AdminOrderDto;
import com.baibei.shiyi.order.feign.base.vo.AdminOrderDetailsVo;
import com.baibei.shiyi.order.feign.base.vo.AdminOrderItemVo;
import com.baibei.shiyi.order.feign.base.vo.AdminOrderSummaryVo;
import com.baibei.shiyi.order.feign.base.vo.AdminOrderVo;
import com.baibei.shiyi.order.model.OrderItem;

import java.util.List;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/07/31 18:16:01
 * @description: OrderItem服务接口
 */
public interface IOrderItemService extends Service<OrderItem> {

    /**
     * 我的订单
     *
     * @param myOrderDto
     * @return
     */
    MyPageInfo<MyOrderVo> myOrderList(MyOrderDto myOrderDto);

    /**
     * 根据订单明细编号查询
     *
     * @param orderItemNo
     * @return
     */
    OrderItem findByOrderItemNo(String orderItemNo);

    /**
     * 根据主订单ID查询
     *
     * @param orderId
     * @return
     */
    List<OrderItem> findByOrderId(Long orderId);

    /**
     * 根据主订单查询子订单
     *
     * @param orderId
     * @return
     */
    List<AdminOrderDetailsVo> findByAdminOrderId(Long orderId);

    /**
     * 查询子订单详情
     * @param orderItemId
     * @return
     */
    List<AdminOrderDetailsVo> findByOrderItemId(Long orderItemId);

    /**
     * 自动收货
     */
    ApiResult autoDelivery();

    /**
     * 子订单列表
     * @param orderDto
     * @return
     */
    List<AdminOrderItemVo> orderAndOrderItemPageList(AdminOrderDto orderDto);

    /**
     * 子订单详情
     * @param orderItemId
     * @return
     */
    AdminOrderVo getByIdToOrderItem(long orderItemId);

    /**
     * 子订单统计
     * @param orderDto
     * @return
     */
    AdminOrderSummaryVo orderItemCount(AdminOrderDto orderDto);

    /**
     * 统计
     * @param status
     * @param orderDto
     * @return
     */
    Integer getOrderItemStatusCount(String status, AdminOrderDto orderDto);
}
