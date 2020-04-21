package com.baibei.shiyi.trade.feign.base;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.trade.feign.bean.dto.CustomerHoldDto;
import com.baibei.shiyi.trade.feign.bean.dto.CustomerHoldPageListDto;
import com.baibei.shiyi.trade.feign.bean.vo.CustomerHoldPageListVo;
import com.baibei.shiyi.trade.feign.bean.vo.CustomerHoldVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author: hyc
 * @date: 2019/11/14 13:57
 * @description:
 */
public interface IAdminCustomerHoldBase {
    /**
     * 持有列表
     * @param customerHoldPageListDto
     * @return
     */
    @PostMapping(value = "/shiyi/admin/trade/customerHold/pageList")
    ApiResult<MyPageInfo<CustomerHoldPageListVo>> pageList(@RequestBody @Validated CustomerHoldPageListDto customerHoldPageListDto);

    /**
     * 导出
     * @param customerHoldPageListDto
     * @return
     */
    @PostMapping(value = "/shiyi/admin/trade/customerHold/export")
    ApiResult<List<CustomerHoldPageListVo>> export(CustomerHoldPageListDto customerHoldPageListDto);
}
