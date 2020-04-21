package com.baibei.shiyi.order.feign.client.hystrix;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.order.feign.base.dto.*;
import com.baibei.shiyi.order.feign.client.IAdminAfterSaleFeign;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AdminAfterSaleHystrix implements FallbackFactory<IAdminAfterSaleFeign> {

    @Override
    public IAdminAfterSaleFeign create(Throwable throwable) {
        return new IAdminAfterSaleFeign(){

            @Override
            public ApiResult createAndLook(AfterSaleOrderDto afterSaleOrderDto) {
                log.info("createAndLook setting is error,exception is {}",throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult pageList(AfterSalePageListDto afterSalePageListDto) {
                log.info("pageList setting is error,exception is {}",throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult exportData(AfterSalePageListDto afterSalePageListDto) {
                log.info("exportData setting is error,exception is {}",throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult details(AfterSaleDetailsDto afterSaleDetailsDro) {
                log.info("details setting is error,exception is {}",throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult operatorApplication(OperatorApplicationDto operatorApplicationDto) {
                log.info("operatorApplication setting is error,exception is {}",throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult defaultSendBackAddress() {
                log.info("defaultSendBackAddress setting is error,exception is {}",throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult confirmReceipt(ConfirmReceiptDto confirmReceiptDto) {
                log.info("confirmReceipt setting is error,exception is {}",throwable);
                return ApiResult.serviceFail();
            }
        };
    }
}
