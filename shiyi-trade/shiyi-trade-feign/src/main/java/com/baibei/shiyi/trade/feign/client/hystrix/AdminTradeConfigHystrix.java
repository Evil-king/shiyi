package com.baibei.shiyi.trade.feign.client.hystrix;

import com.alibaba.fastjson.JSONObject;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.trade.feign.bean.dto.AdminTradeConfigDto;
import com.baibei.shiyi.trade.feign.client.admin.IAdminTradeConfigFeign;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AdminTradeConfigHystrix implements FallbackFactory<IAdminTradeConfigFeign> {
    @Override
    public IAdminTradeConfigFeign create(Throwable throwable) {
        return new IAdminTradeConfigFeign() {
            @Override
            public ApiResult save(AdminTradeConfigDto adminTradeConfigDto) {
                log.info("save is hystrix exception is {},Params is {}", throwable, JSONObject.toJSONString(adminTradeConfigDto));
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<AdminTradeConfigDto> getConfig() {
                log.info("getConfig is hystrix exception is {}", throwable);
                return ApiResult.serviceFail();
            }
        };
    }
}
