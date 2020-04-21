package com.baibei.shiyi.trade.feign.client.hystrix;

import com.alibaba.fastjson.JSONObject;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.trade.feign.bean.dto.HoldPositionDto;
import com.baibei.shiyi.trade.feign.bean.vo.HoldPositionVo;
import com.baibei.shiyi.trade.feign.client.admin.IAdminHoldPositionFeign;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class AdminHoldPositionHystrix implements FallbackFactory<IAdminHoldPositionFeign> {

    @Override
    public IAdminHoldPositionFeign create(Throwable throwable) {
        return new IAdminHoldPositionFeign() {
            @Override
            public ApiResult<MyPageInfo<HoldPositionVo>> pageList(HoldPositionDto holdPositionDto) {
                log.info("current pageList is fallbackFactory,params is {},error is {}", JSONObject.toJSONString(holdPositionDto), throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<List<HoldPositionVo>> exportList(HoldPositionDto holdPositionDto) {
                log.info("current exportList is fallbackFactory,params is {},error is {}",JSONObject.toJSONString(holdPositionDto),throwable);
                return ApiResult.serviceFail();
            }
        };
    }
}
