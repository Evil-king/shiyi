package com.baibei.shiyi.user.feign.client.hystrix;

import com.alibaba.fastjson.JSONObject;
import com.baibei.shiyi.account.feign.bean.dto.CustomerNoDto;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.bean.CustomerBaseDto;
import com.baibei.shiyi.user.feign.bean.vo.PABCustomerVo;
import com.baibei.shiyi.user.feign.bean.vo.RealnameInfoVo;
import com.baibei.shiyi.user.feign.client.shiyi.ICustomerFeign;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PABCustomerHystrix implements FallbackFactory<ICustomerFeign> {

    @Override
    public ICustomerFeign create(Throwable throwable) {
        return new ICustomerFeign() {
            @Override
            public ApiResult update(PABCustomerVo pabCustomerVo) {
                log.info("update fallbackFactory  exception is {}", throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<PABCustomerVo> findCustomerNo(CustomerNoDto customerNoDto) {
                log.info("findCustomerNo fallbackFactory customerNoDto is {}, exception is {}", JSONObject.toJSONString(customerNoDto), throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<RealnameInfoVo> realnameInfo(CustomerBaseDto customerBaseDto) {
                log.info("realnameInfo fallbackFactory customerBaseDto is {} exception is {}", JSONObject.toJSONString(customerBaseDto), throwable);
                return ApiResult.serviceFail();
            }

        };
    }
}
