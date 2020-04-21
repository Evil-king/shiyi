package com.baibei.shiyi.trade.feign.client.hystrix;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.trade.feign.bean.dto.CustomerHoldDto;
import com.baibei.shiyi.trade.feign.bean.vo.CustomerHoldVo;
import com.baibei.shiyi.trade.feign.bean.vo.StatisticsVo;
import com.baibei.shiyi.trade.feign.client.CustomerHoldFeign;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/5/29 11:25 AM
 * @description:
 */
public class CustomerHoldHystrix implements CustomerHoldFeign {


    @Override
    public ApiResult<CustomerHoldVo> customerHoldInfo(@RequestBody @Validated CustomerHoldDto customerHoldDto) {
        return ApiResult.serviceFail();
    }

    @Override
    public ApiResult<StatisticsVo> marketValue(@RequestParam("customerNo") String customerNo) {
        return ApiResult.serviceFail();
    }
}
