package com.baibei.shiyi.order.web.api;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.order.common.dto.*;
import com.baibei.shiyi.order.service.IAfterSaleOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/api/order/afterSale")
public class ApiAfterSaleController {

    @Autowired
    private IAfterSaleOrderService afterSaleOrderService;

    @RequestMapping("/submitApplication")
    public ApiResult submitApplication(@RequestBody SubmitApplicationDto submitApplicationDto){
        if("success".equals(afterSaleOrderService.submitApplication(submitApplicationDto))){
            return ApiResult.success();
        }
        return ApiResult.error();
    }

    @RequestMapping("/pageList")
    public ApiResult apiPageList(@RequestBody ApiAfterSalePageListDto apiAfterSalePageListDto){
        return ApiResult.success(afterSaleOrderService.apiPageList(apiAfterSalePageListDto));
    }

    @RequestMapping("/apiAfterSaleDetails")
    public ApiResult details(@RequestBody ApiAfterSaleDetailsDto apiAfterSaleDetailsDto){
        return afterSaleOrderService.apiAfterSaleDetails(apiAfterSaleDetailsDto);
    }

    @RequestMapping("/cancelApplication")
    public ApiResult cancelApplication(@RequestBody ApiCancelApplicationDto apiCancelApplicationDto){
        return afterSaleOrderService.cancelApplication(apiCancelApplicationDto);
    }

    @RequestMapping("/shipping")
    public ApiResult shipping(@RequestBody ShippingDto shippingDto){
        return afterSaleOrderService.shipping(shippingDto);
    }
}
