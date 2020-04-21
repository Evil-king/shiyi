package com.baibei.shiyi.order.feign.client.hystrix;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.order.feign.base.dto.AdminOrderDto;
import com.baibei.shiyi.order.feign.base.vo.AdminOrderItemVo;
import com.baibei.shiyi.order.feign.base.vo.AdminOrderSummaryVo;
import com.baibei.shiyi.order.feign.base.vo.AdminOrderVo;
import com.baibei.shiyi.order.feign.client.IAdminOrderFeign;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class AdminOrderHystrix implements FallbackFactory<IAdminOrderFeign> {
    @Override
    public IAdminOrderFeign create(Throwable throwable) {
        return new IAdminOrderFeign() {

            @Override
            public ApiResult<MyPageInfo<AdminOrderVo>> pageList(AdminOrderDto orderDto) {
                log.info("pageList hystrix is error,exception is {}", throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<MyPageInfo<AdminOrderItemVo>> orderItemPageList(AdminOrderDto orderDto) {
                log.info("orderItemPageList hystrix is error,exception is {}", throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<AdminOrderVo> getById(AdminOrderDto orderDto) {
                log.info("getById hystrix is error,exception is {}", throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<AdminOrderVo> getByIdToOrderItem(AdminOrderDto orderDto) {
                log.info("getByIdToOrderItem hystrix is error,exception is {}", throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<AdminOrderSummaryVo> orderCount(AdminOrderDto orderDto) {
                log.info("orderCount hystrix is error,exception is {}", throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<AdminOrderSummaryVo> orderItemCount(AdminOrderDto adminOrderDto) {
                log.info("orderItemCount hystrix is error,exception is {}", throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult closeItemOrder(AdminOrderDto orderDto) {
                log.info("closeItemOrder hystrix is error,exception is {}", throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult addLogistics(AdminOrderDto adminOrderDto) {
                log.info("addLogistics hystrix is error,exception is {}", throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult itemRefundAgree(AdminOrderDto orderDto) {
                log.info("itemRefundAgree hystrix is error,exception is {}", throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult itemRefundReject(AdminOrderDto orderDto) {
                log.info("itemRefundReject hystrix is error,exception is {}", throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult closeOrder(AdminOrderDto orderDto) {
                log.info("closeOrder hystrix is error,exception is {}",throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult findByOrderList(AdminOrderDto adminOrderDto) {
                log.info("findByOrderList hystrix is error,exception is {}",throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<List<AdminOrderItemVo>> findByOrderItemListExport(AdminOrderDto adminOrderDto) {
                log.info("findByOrderItemListExport hystrix is error,exception is {}",throwable);
                return ApiResult.serviceFail();
            }
        };
    }
}
