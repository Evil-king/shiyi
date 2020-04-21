package com.baibei.shiyi.order.web.admin;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.order.feign.base.dto.AdminOrderDto;
import com.baibei.shiyi.order.feign.base.vo.AdminOrderItemVo;
import com.baibei.shiyi.order.feign.base.vo.AdminOrderSummaryVo;
import com.baibei.shiyi.order.feign.base.vo.AdminOrderVo;
import com.baibei.shiyi.order.feign.client.IAdminOrderFeign;
import com.baibei.shiyi.order.service.IOrderItemService;
import com.baibei.shiyi.order.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/order")
public class AdminOrderController implements IAdminOrderFeign {

    @Autowired
    private IOrderService orderService;
    @Autowired
    private IOrderItemService orderItemService;

    @Override
    public ApiResult<MyPageInfo<AdminOrderVo>> pageList(@Validated @RequestBody AdminOrderDto orderDto) {
        MyPageInfo<AdminOrderVo> pageInfo = orderService.adminPageList(orderDto);
        return ApiResult.success(pageInfo);
    }

    @Override
    public ApiResult<MyPageInfo<AdminOrderItemVo>> orderItemPageList(@Validated @RequestBody AdminOrderDto orderDto) {
        MyPageInfo<AdminOrderItemVo> pageInfo = orderService.orderAndOrderItemPageList(orderDto);
        return ApiResult.success(pageInfo);
    }

    @Override
    public ApiResult<AdminOrderVo> getById(@RequestBody AdminOrderDto orderDto) {
        if (orderDto.getOrderId() == null) {
            return ApiResult.error("订单Id不能为空");
        }
        return ApiResult.success(orderService.getById(orderDto.getOrderId()));
    }

    @Override
    public ApiResult<AdminOrderVo> getByIdToOrderItem(@RequestBody AdminOrderDto orderDto) {
        if (orderDto.getOrderItemId() == null) {
            return ApiResult.error("子订单Id不能为空");
        }
        return ApiResult.success(orderItemService.getByIdToOrderItem(orderDto.getOrderItemId()));
    }


    @Override
    public ApiResult<AdminOrderSummaryVo> orderCount(@RequestBody AdminOrderDto orderDto) {
        return ApiResult.success(orderService.orderCount(orderDto));
    }


    @Override
    public ApiResult<AdminOrderSummaryVo> orderItemCount(@RequestBody AdminOrderDto orderDto) {
        return ApiResult.success(orderItemService.orderItemCount(orderDto));
    }


    @Override
    public ApiResult closeItemOrder(@RequestBody AdminOrderDto orderDto) {
        if (orderDto.getOrderItemNo() == null) {
            return ApiResult.error("子订单不能为空");
        }
        if (orderDto.getReason() == null) {
            return ApiResult.error("子订单关闭原因不能为空");
        }
        orderService.closeItemOrder(orderDto);
        return ApiResult.success();
    }

    @Override
    public ApiResult addLogistics(@RequestBody AdminOrderDto adminOrderDto) {
        if (adminOrderDto.getOrderItemNo() == null) {
            return ApiResult.error("子订单不能为空");
        }
        orderService.addLogistics(adminOrderDto);
        return ApiResult.success();
    }

    @Override
    public ApiResult itemRefundAgree(@RequestBody AdminOrderDto orderDto) {
        if (orderDto.getRefundId() == null) {
            return ApiResult.error("退款Id不能为空");
        }
        orderService.itemRefundAgree(orderDto);
        return ApiResult.success();
    }

    @Override
    public ApiResult itemRefundReject(@RequestBody AdminOrderDto orderDto) {
        if (orderDto.getRefundId() == null) {
            return ApiResult.error("退款Id不能为空");
        }
        orderService.itemRefundReject(orderDto);
        return ApiResult.success();
    }

    @Override
    public ApiResult closeOrder(@RequestBody AdminOrderDto orderDto) {
        if (orderDto.getOrderNo() == null) {
            return ApiResult.error("订单编号不能为空");
        }
        orderService.closeOrder(orderDto);
        return ApiResult.success();
    }

    @Override
    public ApiResult<List<AdminOrderVo>> findByOrderList(@RequestBody AdminOrderDto adminOrderDto) {
        List<AdminOrderVo> adminOrderVoList = orderService.findByOrderList(adminOrderDto);
        return ApiResult.success(adminOrderVoList);
    }

    @Override
    public ApiResult<List<AdminOrderItemVo>> findByOrderItemListExport(@RequestBody AdminOrderDto adminOrderDto) {
        List<AdminOrderItemVo> adminOrderVoList = orderService.findByOrderItemListExport(adminOrderDto);
        return ApiResult.success(adminOrderVoList);
    }
}
