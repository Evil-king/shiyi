package com.baibei.shiyi.account.feign.client.hystrix;

import com.baibei.shiyi.account.feign.bean.dto.ChangeCustomerBeanDto;
import com.baibei.shiyi.account.feign.bean.dto.CustomerNoDto;
import com.baibei.shiyi.account.feign.bean.vo.CustomerBeanVo;
import com.baibei.shiyi.account.feign.client.CustomerBeanFeign;
import com.baibei.shiyi.common.tool.api.ApiResult;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author: hyc
 * @date: 2019/10/29 17:33
 * @description:
 */
@Component
@Slf4j
public class CustomerBeanHystrix implements FallbackFactory<CustomerBeanFeign> {
    @Override
    public CustomerBeanFeign create(Throwable cause) {
        return new CustomerBeanFeign() {
            @Override
            public ApiResult<CustomerBeanVo> getBalance(CustomerNoDto customerNoDto) {
                log.info("getBalance fallback,reason was : {}",cause);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult changeAmount(ChangeCustomerBeanDto changeAmountDto) {
                log.info("changeAmount fallback,reason was : {}",cause);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult release() {
                log.info("release fallback,reason was : {}",cause);
                return ApiResult.serviceFail();
            }
        };
    }
}
