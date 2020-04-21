package com.baibei.shiyi.order.biz;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.utils.IdWorker;
import com.baibei.shiyi.order.model.Order;
import com.baibei.shiyi.order.model.OrderItem;
import com.baibei.shiyi.order.model.OrderProduct;
import com.baibei.shiyi.order.model.OrderRefund;
import com.baibei.shiyi.order.service.IOrderItemService;
import com.baibei.shiyi.order.service.IOrderProductService;
import com.baibei.shiyi.order.service.IOrderRefundService;
import com.baibei.shiyi.order.service.IOrderService;
import com.baibei.shiyi.product.feign.bean.dto.ChangeStockDto;
import com.baibei.shiyi.product.feign.client.shiyi.ProductFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/8/9 16:46
 * @description: 取消订单
 */
@Component
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class CancelOrderBiz {
    @Autowired
    private IOrderService orderService;
    @Autowired
    private IOrderItemService orderItemService;
    @Autowired
    private IOrderRefundService orderRefundService;
    @Autowired
    private ProductFeign productFeign;
    @Autowired
    private IOrderProductService orderProductService;

    /**
     * 取消订单
     *
     * @param orderNo
     */
    public ApiResult cancel(String orderNo, String cancelReason) {
        Order existOrder = orderService.findByOrderNo(orderNo);
        if (existOrder != null) {
            // 只有待付款取消
            if (Constants.MallOrderStatus.WAIT.equals(existOrder.getStatus())) {
                // 修改订单状态为取消状态
                Order order = new Order();
                order.setStatus(Constants.MallOrderStatus.CANCEL);
                order.setModifyTime(new Date());
                order.setCancelReason(cancelReason);
                order.setCancelTime(new Date());
                orderService.updateOrder(orderNo, Constants.MallOrderStatus.WAIT, order);
                // 增加库存
                List<OrderProduct> orderProductList = orderProductService.findByOrderId(existOrder.getId());
                ApiResult apiResult = addStock(orderProductList, orderNo, existOrder.getCustomerNo());
                if (apiResult.hasFail()) {
                    log.info("取消订单增加库存失败，orderNo={}，apiResult={}", orderNo, apiResult.toString());
                    return apiResult;
                }
            } else {
                log.info("订单{}最新状态为{}，不能取消", orderNo, existOrder.getStatus());
                return ApiResult.success();
            }
        } else {
            OrderItem orderItem = orderItemService.findByOrderItemNo(orderNo);
            if (orderItem == null) {
                log.info("订单不存在，orderNo={}", orderNo);
                return ApiResult.success();
            }
            if (Constants.RefundStatus.APPLY_REFUND.equals(orderItem.getRefundStatus())) {
                log.info("订单审核中，不允许重复取消订单，orderNo={}", orderNo);
                return ApiResult.success();
            }
            if (Constants.MallOrderStatus.UNDELIVERY.equals(orderItem.getStatus())) {
                // 修改订单状态为申请退款状态
                Condition condition = new Condition(OrderItem.class);
                condition.createCriteria().andEqualTo("id", orderItem.getId());
                OrderItem newOrderItem = new OrderItem();
                newOrderItem.setRefundStatus(Constants.RefundStatus.APPLY_REFUND);
                newOrderItem.setModifyTime(new Date());
                orderItemService.updateByConditionSelective(newOrderItem, condition);
                // 增加退款申请记录
                OrderRefund orderRefund = new OrderRefund();
                orderRefund.setId(IdWorker.getId());
                orderRefund.setOrderItemId(orderItem.getId());
                orderRefund.setReviewStatus(Constants.OrderRefundReviewStatus.WAIT);
                orderRefund.setRefundAmount(orderItem.getActualAmount());
                orderRefund.setReason(cancelReason);
                orderRefund.setRefundStatus(Constants.RefundStatus.APPLY_REFUND);
                orderRefundService.save(orderRefund);
            } else {
                log.info("订单{}最新状态为{}，不能取消", orderNo, existOrder.getStatus());
                return ApiResult.success();
            }
        }
        return ApiResult.success();
    }

    /**
     * 增加库存
     *
     * @param orderProductList
     * @param orderNo
     * @param customerNo
     * @return
     */
    private ApiResult addStock(List<OrderProduct> orderProductList, String orderNo, String customerNo) {
        List<ChangeStockDto> changeStockDtoList = new ArrayList<>();
        for (OrderProduct dto : orderProductList) {
            ChangeStockDto changeStockDto = new ChangeStockDto();
            changeStockDto.setShelfId(dto.getShelfId());
            changeStockDto.setSkuId(dto.getSkuId());
            changeStockDto.setChangeCount(new BigDecimal(dto.getQuantity()));
            changeStockDto.setOrderNo(orderNo);
            changeStockDto.setOperatorNo(customerNo);
            changeStockDto.setChangeType(Constants.ProductStockChangeType.TRADE);
            changeStockDto.setRetype(Constants.Retype.IN);
            changeStockDto.setChangeSellCountFlag(false);
            changeStockDtoList.add(changeStockDto);
        }
        ApiResult stockResult = productFeign.batchChangeStock(changeStockDtoList);
        return stockResult;
    }
}