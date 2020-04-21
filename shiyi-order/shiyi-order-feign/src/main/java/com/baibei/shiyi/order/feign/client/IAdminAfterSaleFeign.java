package com.baibei.shiyi.order.feign.client;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.order.feign.base.dto.*;
import com.baibei.shiyi.order.feign.base.vo.AfterSaleOrderVo;
import com.baibei.shiyi.order.feign.client.hystrix.AdminOrderHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(value = "${shiyi-order:shiyi-order}",path = "/admin/order/afterSale", fallbackFactory = AdminOrderHystrix.class)
public interface IAdminAfterSaleFeign {

    @RequestMapping("/create")
    ApiResult createAndLook(@RequestBody AfterSaleOrderDto afterSaleOrderDto);

    @RequestMapping("/pageList")
    ApiResult pageList(@RequestBody AfterSalePageListDto afterSalePageListDto);

    @RequestMapping("/exportData")
    ApiResult<List<AfterSaleOrderVo>> exportData(@RequestBody AfterSalePageListDto afterSalePageListDto);

    @RequestMapping("/details")
    ApiResult details(@RequestBody AfterSaleDetailsDto afterSaleDetailsDro);

    @RequestMapping("/operatorApplication")
    ApiResult operatorApplication(@RequestBody OperatorApplicationDto operatorApplicationDto);

    @RequestMapping("/defaultSendBackAddress")
    ApiResult defaultSendBackAddress();

    @RequestMapping("/confirmReceipt")
    ApiResult confirmReceipt(@RequestBody ConfirmReceiptDto confirmReceiptDto);
}
