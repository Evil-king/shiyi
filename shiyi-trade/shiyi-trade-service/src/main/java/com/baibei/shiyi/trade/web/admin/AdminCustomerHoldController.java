package com.baibei.shiyi.trade.web.admin;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.trade.feign.base.IAdminCustomerHoldBase;
import com.baibei.shiyi.trade.feign.bean.dto.CustomerHoldPageListDto;
import com.baibei.shiyi.trade.feign.bean.vo.CustomerHoldPageListVo;
import com.baibei.shiyi.trade.service.IHoldDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 客户持仓明细
 *
 * @author: hyc
 * @date: 2019/11/14 14:41
 * @description:
 */
@RestController
public class AdminCustomerHoldController implements IAdminCustomerHoldBase {

    @Autowired
    private IHoldDetailsService holdDetailsService;

    @Override
    public ApiResult<MyPageInfo<CustomerHoldPageListVo>> pageList(@RequestBody @Validated CustomerHoldPageListDto customerHoldPageListDto) {
        return ApiResult.success(holdDetailsService.mypageList(customerHoldPageListDto));
    }

    @Override
    public ApiResult<List<CustomerHoldPageListVo>> export(@RequestBody CustomerHoldPageListDto customerHoldPageListDto) {
        return ApiResult.success(holdDetailsService.export(customerHoldPageListDto));
    }
}
