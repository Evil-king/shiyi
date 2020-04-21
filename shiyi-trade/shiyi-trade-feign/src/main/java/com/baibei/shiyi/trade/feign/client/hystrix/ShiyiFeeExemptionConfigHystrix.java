package com.baibei.shiyi.trade.feign.client.hystrix;

import com.alibaba.fastjson.JSONObject;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.trade.feign.bean.dto.FeeExemptionConfigDto;
import com.baibei.shiyi.trade.feign.bean.vo.FeeExemptionConfigVo;
import com.baibei.shiyi.trade.feign.client.shiyi.IShiyiFeeExemptionConfigFeign;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class ShiyiFeeExemptionConfigHystrix implements FallbackFactory<IShiyiFeeExemptionConfigFeign> {
    @Override
    public IShiyiFeeExemptionConfigFeign create(Throwable throwable) {
        return new IShiyiFeeExemptionConfigFeign() {

            @Override
            public ApiResult<List<FeeExemptionConfigVo>> pageList(FeeExemptionConfigDto feeExemptionConfigDto) {
                log.info("current pageList exception is {},params is {}",throwable, JSONObject.toJSONString(feeExemptionConfigDto));
                return ApiResult.serviceFail();
            }
        };
    }
}
