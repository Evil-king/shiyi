package com.baibei.shiyi.user.feign.client.hystrix;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.user.feign.bean.vo.CustomerVo;
import com.baibei.shiyi.user.feign.bean.vo.RealnameInfoVo;
import com.baibei.shiyi.user.feign.client.CustomerDetailFeign;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author: hyc
 * @date: 2019/12/3 19:50
 * @description:
 */
@Component
@Slf4j
public class CustomerDetailHystrix implements FallbackFactory<CustomerDetailFeign> {
    @Override
    public CustomerDetailFeign create(Throwable cause) {
        return new CustomerDetailFeign() {
            @Override
            public ApiResult<RealnameInfoVo> findRealNameByCustomerNo(String customerNo) {
                log.info("findRealNameByCustomerNo fallback,reason was : {}", cause);
                return ApiResult.serviceFail();
            }
        };
    }
}
