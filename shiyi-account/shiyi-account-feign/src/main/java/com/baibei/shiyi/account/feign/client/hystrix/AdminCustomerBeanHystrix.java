package com.baibei.shiyi.account.feign.client.hystrix;

import com.baibei.shiyi.account.feign.bean.vo.CustomerBeanVo;
import com.baibei.shiyi.account.feign.client.AccountFeign;
import com.baibei.shiyi.account.feign.client.AdminCustomerBeanFeign;
import com.baibei.shiyi.common.tool.api.ApiResult;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author: hyc
 * @date: 2019/11/1 16:08
 * @description:
 */
@Component
@Slf4j
public class AdminCustomerBeanHystrix implements FallbackFactory<AdminCustomerBeanFeign> {

    @Override
    public AdminCustomerBeanFeign create(Throwable cause) {
        return new AdminCustomerBeanFeign() {
            @Override
            public ApiResult<CustomerBeanVo> getBeanBalance(String customerNo) {
                log.info("getBalance fallback,reason was : {}",cause);
                return ApiResult.serviceFail();
            }
        };
    }
}
