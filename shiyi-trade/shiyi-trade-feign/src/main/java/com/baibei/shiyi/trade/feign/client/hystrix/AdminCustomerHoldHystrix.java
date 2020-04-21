package com.baibei.shiyi.trade.feign.client.hystrix;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.trade.feign.bean.dto.CustomerHoldPageListDto;
import com.baibei.shiyi.trade.feign.bean.vo.CustomerHoldPageListVo;
import com.baibei.shiyi.trade.feign.client.AdminCustomerHoldFeign;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: hyc
 * @date: 2019/11/14 15:17
 * @description:
 */
@Slf4j
@Component
public class AdminCustomerHoldHystrix  implements FallbackFactory<AdminCustomerHoldFeign> {
    @Override
    public AdminCustomerHoldFeign create(Throwable cause) {
        return new AdminCustomerHoldFeign() {
            @Override
            public ApiResult<MyPageInfo<CustomerHoldPageListVo>> pageList(CustomerHoldPageListDto customerHoldPageListDto) {
                log.info("pageList hystrix is error,exception is {}", cause);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<List<CustomerHoldPageListVo>> export(CustomerHoldPageListDto customerHoldPageListDto) {
                log.info("export hystrix is error,exception is {}", cause);
                return ApiResult.serviceFail();
            }
        };
    }
}
