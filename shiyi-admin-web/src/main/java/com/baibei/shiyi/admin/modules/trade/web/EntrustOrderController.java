package com.baibei.shiyi.admin.modules.trade.web;

import com.baibei.shiyi.admin.modules.account.bean.vo.EnumMessageVo;
import com.baibei.shiyi.admin.modules.utils.ExcelUtils;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.enumeration.EntrustOrderResultEnum;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.trade.feign.bean.dto.CustomerEntrustOrderListDto;
import com.baibei.shiyi.trade.feign.bean.vo.CustomerEntrustOrderListVo;
import com.baibei.shiyi.trade.feign.client.AdminEntrustOrderFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 委托单查询
 * @author: hyc
 * @date: 2019/11/14 17:25
 * @description:
 */
@RestController
@RequestMapping("/admin/trade/entrustOrder")
public class EntrustOrderController {
    @Autowired
    private AdminEntrustOrderFeign adminEntrustOrderFeign;

    @Autowired
    private ExcelUtils excelUtils;

    @RequestMapping("/pageList")
    @PreAuthorize("hasAnyRole(@authorityExpression.find('TRANSACTION_MANAGE_DELEGATION_ORDER_QUERY'))")
    public ApiResult<MyPageInfo<CustomerEntrustOrderListVo>> pageList(@RequestBody CustomerEntrustOrderListDto customerEntrustOrderListDto){
        return adminEntrustOrderFeign.pageList(customerEntrustOrderListDto);
    }

    @RequestMapping("/export")
    @PreAuthorize("hasAnyRole(@authorityExpression.exportFile('TRANSACTION_MANAGE_DELEGATION_ORDER_QUERY'))")
    public void export(@RequestBody CustomerEntrustOrderListDto customerEntrustOrderListDto, HttpServletResponse response){
        ApiResult<List<CustomerEntrustOrderListVo>> CustomerEntrustOrderListVo=adminEntrustOrderFeign.export(customerEntrustOrderListDto);
        excelUtils.defaultExcelExport(CustomerEntrustOrderListVo.getData(),response,CustomerEntrustOrderListVo.class,"委托单查询");
    }

    @RequestMapping("/getEnumMessage")
    public ApiResult<List<EnumMessageVo>> getEnumMessage(){
        List<EnumMessageVo> enumMessageVos=new ArrayList<>();
        EntrustOrderResultEnum[] customerStatusEnums=EntrustOrderResultEnum.values();
        for (int i = 0; i <customerStatusEnums.length ; i++) {
            EnumMessageVo enumMessageVo=new EnumMessageVo();
            enumMessageVo.setCode(customerStatusEnums[i].getCode());
            enumMessageVo.setMessage(customerStatusEnums[i].getMsg());
            enumMessageVos.add(enumMessageVo);
        }
        return ApiResult.success(enumMessageVos);
    }
}
