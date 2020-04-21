package com.baibei.shiyi.order.web.api;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.order.common.dto.MyOrderDetailsDto;
import com.baibei.shiyi.order.common.dto.MyOrderDto;
import com.baibei.shiyi.order.common.vo.MyOrderDetailsVo;
import com.baibei.shiyi.order.common.vo.MyOrderVo;
import com.baibei.shiyi.order.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/8/12 18:19
 * @description: 我的订单
 */
@RestController
@RequestMapping("/auth/api/order/my")
public class AuthApiMyOrderController {
    @Autowired
    private IOrderService orderService;

    /**
     * 分页列表
     *
     * @param myOrderDto
     * @return
     */
    @PostMapping("/pageList")
    public ApiResult<MyPageInfo<MyOrderVo>> pageList(@RequestBody @Validated MyOrderDto myOrderDto) {
        return ApiResult.success(orderService.myOrderList(myOrderDto));
    }

    /**
     * 订单详情
     *
     * @param myOrderDetailsDto
     * @return
     */
    @PostMapping("/orderDetails")
    public ApiResult<MyOrderDetailsVo> orderDetails(@RequestBody @Validated MyOrderDetailsDto myOrderDetailsDto) {
        return ApiResult.success(orderService.orderDetails(myOrderDetailsDto));
    }
}