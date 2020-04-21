package com.baibei.shiyi.order.feign.client;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.order.feign.client.hystrix.ShiyiAutoDeliveryOrderHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/10/8 10:22
 * @description:
 */
@FeignClient(value = "shiyi-order", fallbackFactory = ShiyiAutoDeliveryOrderHystrix.class)
public interface IShiyiAutoDeliveryOrderFeign {

    /**
     * 自动自动收货
     *
     * @return
     */
    @GetMapping("/shiyi/order/autoDelivery")
    ApiResult autoDelivery();
}