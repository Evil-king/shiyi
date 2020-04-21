package com.baibei.shiyi.order.service;

import com.baibei.shiyi.common.core.mybatis.Service;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.order.common.dto.MyOrderDetailsDto;
import com.baibei.shiyi.order.common.dto.MyOrderDto;
import com.baibei.shiyi.order.common.vo.MyOrderDetailsVo;
import com.baibei.shiyi.order.common.vo.MyOrderVo;
import com.baibei.shiyi.order.feign.base.dto.AdminOrderDto;
import com.baibei.shiyi.order.feign.base.vo.AdminOrderItemVo;
import com.baibei.shiyi.order.feign.base.vo.AdminOrderSummaryVo;
import com.baibei.shiyi.order.feign.base.vo.AdminOrderVo;
import com.baibei.shiyi.order.model.Order;
import com.baibei.shiyi.order.model.OrderSetting;

import java.util.List;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/07/31 18:16:01
 * @description: Order服务接口
 */
public interface IOrderService extends Service<Order> {


    /**
     * 创建订单
     *
     * @param customerNo
     * @param remark
     * @param addressId
     * @param orderSetting
     * @return
     */
    Order createOrder(String customerNo, String remark, Long addressId, OrderSetting orderSetting);


    /**
     * 根据订单号查询
     *
     * @param orderNo
     * @return
     */
    Order findByOrderNo(String orderNo);

    /**
     * 更新订单
     *
     * @param orderNo
     * @param status
     * @param order
     * @return
     */
    boolean updateOrder(String orderNo, String status, Order order);

    /**
     * 我的订单
     *
     * @param myOrderDto
     * @return
     */
    MyPageInfo<MyOrderVo> myOrderList(MyOrderDto myOrderDto);

    /**
     * 订单详情
     *
     * @param myOrderDetailsDto
     * @return
     */
    MyOrderDetailsVo orderDetails(MyOrderDetailsDto myOrderDetailsDto);

    /**
     * 主订单列表
     *
     * @param orderDto
     * @return
     */
    MyPageInfo<AdminOrderVo> adminPageList(AdminOrderDto orderDto);

    /**
     * 子订单列表
     * @param orderDto
     * @return
     */
    MyPageInfo<AdminOrderItemVo> orderAndOrderItemPageList(AdminOrderDto orderDto);

    /**
     * 订单汇总
     *
     * @return
     */
    AdminOrderSummaryVo orderCount(AdminOrderDto orderDto);

    /**
     * 订单详情
     *
     * @param Id
     * @return
     */
    AdminOrderVo getById(Long Id);

    /**
     * 添加物流信息(并收货)
     *
     * @param orderDto
     */
    void addLogistics(AdminOrderDto orderDto);

    /**
     * 关闭子订单
     *
     * @param orderDto
     */
    void closeItemOrder(AdminOrderDto orderDto);

    /**
     * 退款同意
     *
     * @param orderDto
     */
    void itemRefundAgree(AdminOrderDto orderDto);

    /**
     * 退款拒绝
     *
     * @param orderDto
     */
    void itemRefundReject(AdminOrderDto orderDto);


    /**
     * 根据不同的状态进行汇总
     *
     * @param status
     * @return
     */
    Integer getOrderStatusCount(String status, AdminOrderDto orderDto);

    /**
     * 关闭订单
     *
     * @param orderDto
     */
    void closeOrder(AdminOrderDto orderDto);


    /**
     * 查询订单列表
     *
     * @return
     */
    List<AdminOrderVo> findByOrderList(AdminOrderDto orderDto);


    /**
     * 子订单列表
     * @param orderDto
     * @return
     */
    List<AdminOrderItemVo> findByOrderItemListExport(AdminOrderDto orderDto);

}
