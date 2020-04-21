package com.baibei.shiyi.trade.feign.client.hystrix;

import com.alibaba.fastjson.JSONObject;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.trade.feign.bean.dto.FeeExemptionConfigDto;
import com.baibei.shiyi.trade.feign.bean.vo.FeeExemptionConfigVo;
import com.baibei.shiyi.trade.feign.client.admin.IAdminFeeExemptionConfigFeign;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AdminFeeExemptionConfigHystrix implements FallbackFactory<IAdminFeeExemptionConfigFeign> {

    @Override
    public IAdminFeeExemptionConfigFeign create(Throwable throwable) {
        return new IAdminFeeExemptionConfigFeign() {
            @Override
            public ApiResult<MyPageInfo<FeeExemptionConfigVo>> pageList(FeeExemptionConfigDto feeExemptionDto) {
                log.info("current pageList exception is {},params is {}", throwable, JSONObject.toJSONString(feeExemptionDto));
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult add(FeeExemptionConfigDto tradeFeeExemptionDto) {
                log.info("current add exception is {}, params is {}",throwable,JSONObject.toJSONString(tradeFeeExemptionDto));
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult delete(FeeExemptionConfigDto tradeFeeExemptionDto) {
                log.info("current delete exception is {},params is {}",throwable,JSONObject.toJSONString(tradeFeeExemptionDto));
                return ApiResult.serviceFail();
            }
        };
    }
}
