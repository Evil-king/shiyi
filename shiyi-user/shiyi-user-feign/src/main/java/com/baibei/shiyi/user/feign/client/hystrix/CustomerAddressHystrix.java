package com.baibei.shiyi.user.feign.client.hystrix;

import com.baibei.shiyi.account.feign.bean.dto.CustomerNoDto;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.user.feign.bean.vo.CustomerAddressVo;
import com.baibei.shiyi.user.feign.bean.vo.CustomerVo;
import com.baibei.shiyi.user.feign.client.CustomerAddressFeign;
import com.baibei.shiyi.user.feign.client.CustomerFeign;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author: hyc
 * @date: 2019/5/24 1:52 PM
 * @description:
 */
@Component
@Slf4j
public class CustomerAddressHystrix implements FallbackFactory<CustomerAddressFeign> {



    @Override
    public CustomerAddressFeign create(Throwable cause) {
        return new CustomerAddressFeign() {
            @Override
            public ApiResult<List<CustomerAddressVo>> findAddressByCustomerNo(String customerNo) {
                log.info("findAddressByCustomerNo fallback,reason was : {}",cause);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<CustomerAddressVo> getCustomerAddressById(Long id) {
                log.info("getCustomerAddressById fallback,reason was : {}",cause);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<CustomerAddressVo> getDefaultAddressByNo(String customerNo) {
                log.info("getDefaultAddressByNo fallback,reason was : {}",cause);
                return ApiResult.serviceFail();
            }
        };
    }
}
