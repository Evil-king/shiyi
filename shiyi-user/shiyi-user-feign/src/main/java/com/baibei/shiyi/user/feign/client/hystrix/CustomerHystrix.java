package com.baibei.shiyi.user.feign.client.hystrix;

import com.baibei.shiyi.account.feign.bean.dto.CustomerNoDto;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.user.feign.bean.vo.CustomerVo;
import com.baibei.shiyi.user.feign.bean.vo.PABCustomerVo;
import com.baibei.shiyi.user.feign.client.CustomerFeign;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author: hyc
 * @date: 2019/5/24 1:52 PM
 * @description:
 */
@Component
@Slf4j
public class CustomerHystrix implements FallbackFactory<CustomerFeign> {

    @Override
    public CustomerFeign create(Throwable cause) {
        return new CustomerFeign() {

            @Override
            public ApiResult<CustomerVo> findUserByCustomerNo(CustomerNoDto customerNoDto) {
                log.info("findUserByCustomerNo fallback,reason was : {}", cause);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<String> findCustomerNoByMobile(String mobile) {
                log.info("findCustomerNoByMobile fallback,reason was : {}", cause);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<PABCustomerVo> findByCustomerNo(String customerNo) {
                log.info("findByCustomerNo fallback,reason was : {}", cause);
                return ApiResult.serviceFail();
            }
        };
    }
}
