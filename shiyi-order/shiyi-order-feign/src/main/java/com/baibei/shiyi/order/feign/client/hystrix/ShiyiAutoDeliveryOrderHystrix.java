package com.baibei.shiyi.order.feign.client.hystrix;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.order.feign.client.IShiyiAutoDeliveryOrderFeign;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/10/8 10:25
 * @description:
 */
@Component
@Slf4j
public class ShiyiAutoDeliveryOrderHystrix implements FallbackFactory<IShiyiAutoDeliveryOrderFeign> {

    @Override
    public IShiyiAutoDeliveryOrderFeign create(Throwable throwable) {
        return new IShiyiAutoDeliveryOrderFeign() {
            @Override
            public ApiResult autoDelivery() {
                log.info("autoDelivery fallback，reason was:", throwable);
                return ApiResult.serviceFail();
            }
        };
    }
}