package com.baibei.shiyi.order.web.shiyi;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.order.feign.client.IShiyiAutoDeliveryOrderFeign;
import com.baibei.shiyi.order.service.IOrderItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/10/8 10:30
 * @description:
 */
@RestController
@Slf4j
public class ShiyiAutoDeliveryOrderController implements IShiyiAutoDeliveryOrderFeign {
    @Autowired
    private IOrderItemService orderItemService;

    @Override
    public ApiResult autoDelivery() {
        log.info("正在执行订单自动收货...");
        return orderItemService.autoDelivery();
    }
}