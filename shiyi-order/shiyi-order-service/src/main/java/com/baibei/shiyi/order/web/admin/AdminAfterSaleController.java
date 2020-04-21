package com.baibei.shiyi.order.web.admin;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.order.feign.base.dto.*;
import com.baibei.shiyi.order.feign.base.vo.AfterSaleOrderVo;
import com.baibei.shiyi.order.feign.client.IAdminAfterSaleFeign;
import com.baibei.shiyi.order.model.Order;
import com.baibei.shiyi.order.model.OrderItem;
import com.baibei.shiyi.order.service.IAfterSaleOrderService;
import com.baibei.shiyi.order.service.IOrderItemService;
import com.baibei.shiyi.order.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/order/afterSale")
public class AdminAfterSaleController implements IAdminAfterSaleFeign {

    @Value("${default.sendback.adress}")
    private String defaultSendBackAddress;
    @Autowired
    private IAfterSaleOrderService afterSaleOrderService;
    @Autowired
    private IOrderItemService orderItemService;
    @Autowired
    private IOrderService orderService;

    public ApiResult createAndLook(@RequestBody AfterSaleOrderDto afterSaleOrderDto){
        Order byOrderNo = orderService.findByOrderNo(afterSaleOrderDto.getOrderNo());
        OrderItem byOrderItemNo = orderItemService.findByOrderItemNo(afterSaleOrderDto.getOrderItemNo());
        return afterSaleOrderService.createOrder(byOrderItemNo,byOrderNo);
    }

    public ApiResult pageList(@RequestBody AfterSalePageListDto afterSalePageListDto){
        return ApiResult.success(afterSaleOrderService.pageListData(afterSalePageListDto));
    }

    public ApiResult<List<AfterSaleOrderVo>> exportData(@RequestBody AfterSalePageListDto afterSalePageListDto){

        return afterSaleOrderService.exportData(afterSalePageListDto);
    }

    public ApiResult details(@RequestBody AfterSaleDetailsDto afterSaleDetailsDro){

        return ApiResult.success(afterSaleOrderService.details(afterSaleDetailsDro.getOrderItemNo()));
    }

    public ApiResult operatorApplication(@RequestBody OperatorApplicationDto operatorApplicationDto){

        return afterSaleOrderService.operatorApplication(operatorApplicationDto);
    }

    public ApiResult defaultSendBackAddress(){
        return ApiResult.success(defaultSendBackAddress);
    }

    public ApiResult confirmReceipt(@RequestBody ConfirmReceiptDto confirmReceiptDto){

        return afterSaleOrderService.confirmReceipt(confirmReceiptDto);
    }

}
