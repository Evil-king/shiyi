package com.baibei.shiyi.order.web.api;

import com.baibei.shiyi.common.core.aop.NoRepeatSubmit;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.order.biz.CancelOrderBiz;
import com.baibei.shiyi.order.biz.OrderBiz;
import com.baibei.shiyi.order.common.dto.*;
import com.baibei.shiyi.order.common.vo.OrderPayInfoVo;
import com.baibei.shiyi.order.common.vo.OrderPayVo;
import com.baibei.shiyi.order.common.vo.OrderSubmitVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/8/2 13:58
 * @description: 订单
 */
@RestController
@RequestMapping("/auth/api/order")
public class AuthApiOrderController {
    @Autowired
    private OrderBiz orderBiz;
    @Autowired
    private CancelOrderBiz cancelOrderBiz;

    /**
     * 确认订单页面，获取支付数据信息
     *
     * @return
     */
    @PostMapping("/payInfo")
    public ApiResult<OrderPayInfoVo> payInfo(@RequestBody @Validated OrderPayInfoDto orderPayInfoDto) {
        return orderBiz.payInfo(orderPayInfoDto);
    }

    /**
     * 提交订单
     *
     * @return
     */
    @PostMapping("/submit")
    @NoRepeatSubmit
    public ApiResult<OrderSubmitVo> submit(@RequestBody @Validated OrderSubmitDto orderSubmitDto) {
        return orderBiz.submit(orderSubmitDto);
    }


    /**
     * 确认支付
     *
     * @return
     */
    @PostMapping("/pay")
    @NoRepeatSubmit
    public ApiResult<OrderPayVo> pay(@RequestBody @Validated OrderPayDto orderPayDto) {
        return orderBiz.pay(orderPayDto);
    }

    /**
     * 订单取消
     *
     * @return
     */
    @PostMapping("/cancel")
    public ApiResult cancel(@RequestBody @Validated OrderCancelDto orderCancelDto) {
        return cancelOrderBiz.cancel(orderCancelDto.getOrderNo(), orderCancelDto.getReason());
    }

    /**
     * 订单确认收货
     *
     * @return
     */
    @PostMapping("/confirmDelivery")
    public ApiResult confirmDelivery(@RequestBody @Validated OrderNoDto orderNoDto) {
        return orderBiz.confirmDelivery(orderNoDto.getOrderNo(), orderNoDto.getCustomerNo());
    }

    /**
     * 删除订单
     *
     * @return
     */
    @PostMapping("/delete")
    public ApiResult delete(@RequestBody @Validated OrderNoDto orderNoDto) {
        return orderBiz.delete(orderNoDto.getOrderNo(), orderNoDto.getCustomerNo());
    }
}