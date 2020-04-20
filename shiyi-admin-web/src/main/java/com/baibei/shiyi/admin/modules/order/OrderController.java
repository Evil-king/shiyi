package com.baibei.shiyi.admin.modules.order;

import com.baibei.shiyi.admin.modules.utils.ExcelUtils;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.exception.ServiceException;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.order.feign.base.dto.AdminOrderDto;
import com.baibei.shiyi.order.feign.base.vo.AdminOrderItemVo;
import com.baibei.shiyi.order.feign.base.vo.AdminOrderSummaryVo;
import com.baibei.shiyi.order.feign.base.vo.AdminOrderVo;
import com.baibei.shiyi.order.feign.client.IAdminOrderFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/8/5 18:27
 * @description:
 */
@RestController
@RequestMapping("/admin/order")
public class OrderController {

    @Autowired
    private IAdminOrderFeign orderFeign;

    @Autowired
    private ExcelUtils excelUtils;

    @PostMapping(path = "/pageList")
    @PreAuthorize("hasAnyRole(@authorityExpression.find('ORDER_MASTER_ORDER'))")
    public ApiResult<MyPageInfo<AdminOrderVo>> pageList(@Validated @RequestBody AdminOrderDto adminOrderDto) {
        return orderFeign.pageList(adminOrderDto);
    }

    @PostMapping(path = "/orderItemPageList")
    @PreAuthorize("hasAnyRole(@authorityExpression.find('ORDER_SUB_ORDER'))")
    public ApiResult<MyPageInfo<AdminOrderItemVo>> orderItemPageList(@Validated @RequestBody AdminOrderDto adminOrderDto) {
        return orderFeign.orderItemPageList(adminOrderDto);
    }

    @PostMapping(path = "/getById")
    @PreAuthorize("hasAnyRole(@authorityExpression.edit('ORDER_MASTER_ORDER'))")
    public ApiResult<AdminOrderVo> getById(@RequestBody AdminOrderDto adminOrderDto) {
        return orderFeign.getById(adminOrderDto);
    }

    @PostMapping(path = "/getByIdToOrderItem")
    @PreAuthorize("hasAnyRole(@authorityExpression.edit('ORDER_SUB_ORDER'))")
    public ApiResult<AdminOrderVo> getByIdToOrderItem(@RequestBody AdminOrderDto adminOrderDto) {
        return orderFeign.getByIdToOrderItem(adminOrderDto);
    }

    @PostMapping(path = "/addLogistics")
    public ApiResult addLogistics(@RequestBody AdminOrderDto adminOrderDto) {
        return orderFeign.addLogistics(adminOrderDto);
    }

    @PostMapping(path = "/closeItemOrder")
    public ApiResult closeItemOrder(@RequestBody AdminOrderDto orderDto) {
        return orderFeign.closeItemOrder(orderDto);
    }

    @PostMapping(path = "/itemRefundAgree")
    public ApiResult itemRefundAgree(@RequestBody AdminOrderDto orderDto) {
        return orderFeign.itemRefundAgree(orderDto);
    }

    @PostMapping(path = "/itemRefundReject")
    public ApiResult itemRefundReject(@RequestBody AdminOrderDto orderDto) {
        return orderFeign.itemRefundReject(orderDto);
    }

    @PostMapping(path = "/orderCount")
    public ApiResult<AdminOrderSummaryVo> orderCount(@RequestBody AdminOrderDto orderDto) {
        return orderFeign.orderCount(orderDto);
    }

    @PostMapping(path = "/orderItemCount")
    public ApiResult<AdminOrderSummaryVo> orderItemCount(@RequestBody AdminOrderDto orderDto) {
        return orderFeign.orderItemCount(orderDto);
    }

    @PostMapping(path = "/closeOrder")
    public ApiResult closeOrder(@RequestBody AdminOrderDto orderDto) {
        return orderFeign.closeOrder(orderDto);
    }

    /**
     * @param adminOrderDto
     * @param response
     */
    @PostMapping(path = "/excelExport", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @PreAuthorize("hasAnyRole(@authorityExpression.exportFile('ORDER_MASTER_ORDER'))")
    public void excelExport(@RequestBody AdminOrderDto adminOrderDto, HttpServletResponse response) {
        ApiResult<List<AdminOrderVo>> listApiResult = orderFeign.findByOrderList(adminOrderDto);
        if (listApiResult.getCode().equals(ApiResult.serviceFail().getCode())) {
            throw new ServiceException("导出失败:" + listApiResult.getMsg());
        }
        excelUtils.defaultExcelExport(listApiResult.getData(), response, AdminOrderVo.class, "订单列表");
    }

    /**
     * @param adminOrderDto
     * @param response
     */
    @PostMapping(path = "/excelOrderItemExport", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @PreAuthorize("hasAnyRole(@authorityExpression.exportFile('ORDER_SUB_ORDER'))")
    public void excelOrderItemExport(@RequestBody AdminOrderDto adminOrderDto, HttpServletResponse response) {
        ApiResult<List<AdminOrderItemVo>> listApiResult = orderFeign.findByOrderItemListExport(adminOrderDto);
        if (listApiResult.getCode().equals(ApiResult.serviceFail().getCode())) {
            throw new ServiceException("导出失败:" + listApiResult.getMsg());
        }
        excelUtils.defaultExcelExport(listApiResult.getData(), response, AdminOrderItemVo.class, "订单列表");
    }

}