package com.baibei.shiyi.trade.feign.client.hystrix;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.trade.feign.bean.dto.DealOrderDto;
import com.baibei.shiyi.trade.feign.bean.vo.DealOrderVo;
import com.baibei.shiyi.trade.feign.client.AdminDealOrderFeign;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: hyc
 * @date: 2019/11/18 10:33
 * @description:
 */
@Component
@Slf4j
public class AdminDealOrderHystrix implements FallbackFactory<AdminDealOrderFeign> {
    @Override
    public AdminDealOrderFeign create(Throwable cause) {
        return new AdminDealOrderFeign() {
            @Override
            public ApiResult<MyPageInfo<DealOrderVo>> pageList(DealOrderDto dealOrderDto) {
                log.info("pageList hystrix is error,exception is {}", cause);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<List<DealOrderVo>> List(DealOrderDto dealOrderDto) {
                log.info("List hystrix is error,exception is {}", cause);
                return ApiResult.serviceFail();
            }
        };
    }
}
