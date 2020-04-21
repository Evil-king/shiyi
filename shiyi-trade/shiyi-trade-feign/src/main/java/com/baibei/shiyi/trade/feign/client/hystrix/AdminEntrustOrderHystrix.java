package com.baibei.shiyi.trade.feign.client.hystrix;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.trade.feign.bean.dto.CustomerEntrustOrderListDto;
import com.baibei.shiyi.trade.feign.bean.vo.CustomerEntrustOrderListVo;
import com.baibei.shiyi.trade.feign.client.AdminEntrustOrderFeign;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: hyc
 * @date: 2019/11/14 16:22
 * @description:
 */
@Slf4j
@Component
public class AdminEntrustOrderHystrix implements FallbackFactory<AdminEntrustOrderFeign> {
    @Override
    public AdminEntrustOrderFeign create(Throwable cause) {
        return new AdminEntrustOrderFeign() {
            @Override
            public ApiResult<MyPageInfo<CustomerEntrustOrderListVo>> pageList(CustomerEntrustOrderListDto customerEntrustOrderListDto) {
                log.info("pageList hystrix is error,exception is {}", cause);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<List<CustomerEntrustOrderListVo>> export(CustomerEntrustOrderListDto customerEntrustOrderListDto) {
                log.info("export hystrix is error,exception is {}", cause);
                return ApiResult.serviceFail();
            }
        };
    }
}
