package com.baibei.shiyi.admin.modules.trade.web;

import com.baibei.shiyi.admin.modules.utils.ExcelUtils;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.trade.feign.bean.dto.CustomerHoldPageListDto;
import com.baibei.shiyi.trade.feign.bean.vo.CustomerHoldPageListVo;
import com.baibei.shiyi.trade.feign.client.AdminCustomerHoldFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 持仓明细
 * @author: hyc
 * @date: 2019/11/14 15:15
 * @description:
 */
@RestController
@RequestMapping("/admin/trade/holdDetail")
public class HoldDetailController {
    @Autowired
    private AdminCustomerHoldFeign adminCustomerHoldFeign;

    @Autowired
    private ExcelUtils excelUtils;

    @RequestMapping("/pageList")
    @PreAuthorize("hasAnyRole(@authorityExpression.find('TRANSACTION_MANAGE_POSITION_DETAILS'))")
    public ApiResult<MyPageInfo<CustomerHoldPageListVo>> pageList(@RequestBody @Validated CustomerHoldPageListDto customerHoldPageListDto){
        return adminCustomerHoldFeign.pageList(customerHoldPageListDto);
    }

    @RequestMapping("/export")
    @PreAuthorize("hasAnyRole(@authorityExpression.exportFile('TRANSACTION_MANAGE_POSITION_DETAILS'))")
    public void export(@RequestBody  CustomerHoldPageListDto customerHoldPageListDto, HttpServletResponse response){
        ApiResult <List<CustomerHoldPageListVo>> customerHoldPageListVos=adminCustomerHoldFeign.export(customerHoldPageListDto);
        excelUtils.defaultExcelExport(customerHoldPageListVos.getData(),response,CustomerHoldPageListVo.class,"持仓明细");
    }


}
