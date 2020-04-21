package com.baibei.shiyi.order.feign.client.hystrix;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.order.feign.base.dto.AdminOrderSettingDto;
import com.baibei.shiyi.order.feign.base.vo.AdminOrderSettingVo;
import com.baibei.shiyi.order.feign.client.IAdminOrderSettingFeign;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AdminOrderSettingHystrix implements FallbackFactory<IAdminOrderSettingFeign> {
    @Override
    public IAdminOrderSettingFeign create(Throwable throwable) {
        return new IAdminOrderSettingFeign() {
            @Override
            public ApiResult setting(AdminOrderSettingDto adminOrderSettingDto) {
                log.info("current setting is error,exception is {}",throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<AdminOrderSettingVo> findBySetting() {
                log.info("current findBySetting is error,exception is {}",throwable);
                return ApiResult.serviceFail();
            }
        };
    }
}
