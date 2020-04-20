package com.baibei.shiyi.cash.feign.client.hystrix;

import com.alibaba.fastjson.JSONObject;
import com.baibei.shiyi.cash.feign.base.dto.PABDepositDto;
import com.baibei.shiyi.cash.feign.base.vo.PABDepositVo;
import com.baibei.shiyi.cash.feign.client.shiyi.IShiyiDepositFeign;
import com.baibei.shiyi.common.tool.api.ApiResult;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DepositHystrix implements FallbackFactory<IShiyiDepositFeign> {
    @Override
    public IShiyiDepositFeign create(Throwable throwable) {
        return new IShiyiDepositFeign() {
            @Override
            public ApiResult<PABDepositVo> deposit(PABDepositDto pabDepositDto) {
                log.info("Deposit fallbackFactory is Hystrix,params is {},exception is {}", JSONObject.toJSONString(pabDepositDto), throwable);
                return ApiResult.serviceFail();
            }
        };
    }
}
