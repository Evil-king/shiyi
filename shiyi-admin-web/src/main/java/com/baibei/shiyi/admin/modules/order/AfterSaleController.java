package com.baibei.shiyi.admin.modules.order;

import com.baibei.shiyi.admin.modules.utils.ExcelUtils;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.exception.ServiceException;
import com.baibei.shiyi.order.feign.base.dto.*;
import com.baibei.shiyi.order.feign.base.vo.AfterSaleOrderVo;
import com.baibei.shiyi.order.feign.client.IAdminAfterSaleFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 售后订单
 */
@RestController
@RequestMapping("/admin/order/afterSale")
public class AfterSaleController {

    @Autowired
    private IAdminAfterSaleFeign adminAfterSaleFeign;
    @Autowired
    private ExcelUtils excelUtils;


    @RequestMapping("/create")
    @PreAuthorize("hasAnyRole(@authorityExpression.add('ORDER_AFTER_SALE'))")
    public ApiResult createAndLook(@RequestBody AfterSaleOrderDto afterSaleOrderDto) {

        return adminAfterSaleFeign.createAndLook(afterSaleOrderDto);
    }

    @RequestMapping("/pageList")
    @PreAuthorize("hasAnyRole(@authorityExpression.find('ORDER_AFTER_SALE'))")
    public ApiResult pageList(@RequestBody AfterSalePageListDto afterSalePageListDto) {
        return adminAfterSaleFeign.pageList(afterSalePageListDto);
    }

    @RequestMapping(path = "/exportData", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @PreAuthorize("hasAnyRole(@authorityExpression.exportFile('ORDER_AFTER_SALE'))")
    public void exportData(@RequestBody AfterSalePageListDto afterSalePageListDto, HttpServletResponse response) {
        ApiResult<List<AfterSaleOrderVo>> listApiResult = adminAfterSaleFeign.exportData(afterSalePageListDto);
        if (listApiResult.getCode().equals(ApiResult.serviceFail().getCode())) {
            throw new ServiceException("导出失败:" + listApiResult.getMsg());
        }
        excelUtils.defaultExcelExport(listApiResult.getData(), response, AfterSaleOrderVo.class, "订单列表");
    }

    @RequestMapping("/details")
    @PreAuthorize("hasAnyRole(@authorityExpression.edit('ORDER_AFTER_SALE'))")
    public ApiResult details(@RequestBody AfterSaleDetailsDto afterSaleDetailsDro) {

        return adminAfterSaleFeign.details(afterSaleDetailsDro);
    }

    @RequestMapping("/operatorApplication")
    public ApiResult operatorApplication(@RequestBody OperatorApplicationDto operatorApplicationDto) {

        return adminAfterSaleFeign.operatorApplication(operatorApplicationDto);
    }

    @RequestMapping("/defaultSendBackAddress")
    public ApiResult defaultSendBackAddress() {
        return adminAfterSaleFeign.defaultSendBackAddress();
    }

    @RequestMapping("/confirmReceipt")
    public ApiResult confirmReceipt(@RequestBody ConfirmReceiptDto confirmReceiptDto) {

        return adminAfterSaleFeign.confirmReceipt(confirmReceiptDto);
    }
}
