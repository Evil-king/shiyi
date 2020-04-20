package com.baibei.shiyi.admin.modules.trade.web;

import com.baibei.shiyi.admin.modules.account.bean.vo.EnumMessageVo;
import com.baibei.shiyi.admin.modules.utils.ExcelUtils;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.enumeration.DealOrderTypeEnum;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.trade.feign.bean.dto.DealOrderDto;
import com.baibei.shiyi.trade.feign.bean.vo.DealOrderVo;
import com.baibei.shiyi.trade.feign.client.AdminDealOrderFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 成交记录查询
 *
 * @author: hyc
 * @date: 2019/11/19 9:53
 * @description:
 */
@RestController
@RequestMapping("/admin/trade/dealOrder")
public class DealOrderController {
    @Autowired
    private AdminDealOrderFeign dealOrderFeign;

    @Autowired
    private ExcelUtils excelUtils;

    @RequestMapping("/pageList")
    @PreAuthorize("hasAnyRole(@authorityExpression.find('TRANSACTION_MANAGE_RECORD_QUERY'))")
    public ApiResult<MyPageInfo<DealOrderVo>> pageList(@RequestBody @Validated DealOrderDto dealOrderDto) {
        return dealOrderFeign.pageList(dealOrderDto);
    }

    @RequestMapping("/export")
    @PreAuthorize("hasAnyRole(@authorityExpression.exportFile('TRANSACTION_MANAGE_RECORD_QUERY'))")
    public void export(@RequestBody DealOrderDto dealOrderDto, HttpServletResponse response) {
        ApiResult<List<DealOrderVo>> dealOrders = dealOrderFeign.List(dealOrderDto);
        excelUtils.defaultExcelExport(dealOrders.getData(), response, DealOrderVo.class, "成交单查询");

    }

    @RequestMapping("/getEnumMessage")
    public ApiResult<List<EnumMessageVo>> getEnumMessage() {
        List<EnumMessageVo> enumMessageVos = new ArrayList<>();
        DealOrderTypeEnum[] dealOrderTypeEnums = DealOrderTypeEnum.values();
        for (int i = 0; i < dealOrderTypeEnums.length; i++) {
            EnumMessageVo enumMessageVo = new EnumMessageVo();
            enumMessageVo.setCode(dealOrderTypeEnums[i].getCode());
            enumMessageVo.setMessage(dealOrderTypeEnums[i].getMsg());
            enumMessageVos.add(enumMessageVo);
        }
        return ApiResult.success(enumMessageVos);
    }
}
