package com.baibei.shiyi.order.feign.client;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.order.feign.base.dto.AdminOrderDto;
import com.baibei.shiyi.order.feign.base.vo.AdminOrderItemVo;
import com.baibei.shiyi.order.feign.base.vo.AdminOrderSummaryVo;
import com.baibei.shiyi.order.feign.base.vo.AdminOrderVo;
import com.baibei.shiyi.order.feign.client.hystrix.AdminOrderHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "${shiyi-order:shiyi-order}",path = "/admin/order", fallbackFactory = AdminOrderHystrix.class)
public interface IAdminOrderFeign {

    /**
     * 主订单分页列表
     *
     * @param orderDto
     * @return
     */
    @PostMapping(path = "/pageList")
    ApiResult<MyPageInfo<AdminOrderVo>> pageList(@Validated @RequestBody AdminOrderDto orderDto);

    /**
     * 子订单分页列表
     *
     * @param orderDto
     * @return
     */
    @PostMapping(path = "/orderItemPageList")
    ApiResult<MyPageInfo<AdminOrderItemVo>> orderItemPageList(@Validated @RequestBody AdminOrderDto orderDto);

    /**
     * 主订单详情
     */
    @PostMapping(path = "/getById")
    ApiResult<AdminOrderVo> getById(@RequestBody AdminOrderDto orderDto);


    /**
     * 子订单订单详情
     */
    @PostMapping(path = "/getByIdToOrderItem")
    ApiResult<AdminOrderVo> getByIdToOrderItem(@RequestBody AdminOrderDto orderDto);


    /**
     * 主订单总数汇总
     *
     * @return
     */
    @PostMapping(path = "/orderCount")
    ApiResult<AdminOrderSummaryVo> orderCount(AdminOrderDto adminOrderDto);


    /**
     * 子订单总数汇总
     *
     * @return
     */
    @PostMapping(path = "/orderItemCount")
    ApiResult<AdminOrderSummaryVo> orderItemCount(AdminOrderDto adminOrderDto);


    /**
     * 关闭子订单
     *
     * @return
     */
    @PostMapping(path = "/closeItemOrder")
    ApiResult closeItemOrder(@RequestBody AdminOrderDto orderDto);


    /**
     * 添加物流信息
     *
     * @return
     */
    @PostMapping(path = "/addLogistics")
    ApiResult addLogistics(@RequestBody AdminOrderDto adminOrderDto);

    /**
     * 子订单退款同意
     *
     * @return
     */
    @PostMapping(path = "/itemRefundAgree")
    ApiResult itemRefundAgree(@RequestBody AdminOrderDto orderDto);

    /**
     * 子订单退款拒绝
     *
     * @param orderDto
     * @return
     */
    @PostMapping(path = "/itemRefundReject")
    ApiResult itemRefundReject(@RequestBody AdminOrderDto orderDto);


    /**
     * 关闭主订单
     *
     * @param orderDto
     * @return
     */
    @PostMapping(path = "/closeOrder")
    ApiResult closeOrder(@RequestBody AdminOrderDto orderDto);


    /**
     * 查询订单数据
     *
     * @return
     */
    @PostMapping(path = "/findByOrderList")
    ApiResult<List<AdminOrderVo>> findByOrderList(@RequestBody AdminOrderDto adminOrderDto);


    /**
     * 查询订单数据
     *
     * @return
     */
    @PostMapping(path = "/findByOrderItemListExport")
    ApiResult<List<AdminOrderItemVo>> findByOrderItemListExport(@RequestBody AdminOrderDto adminOrderDto);
}


