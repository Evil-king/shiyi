package com.baibei.shiyi.trade.web.admin;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.trade.feign.base.IAdminEntrustOrderBase;
import com.baibei.shiyi.trade.feign.bean.dto.CustomerEntrustOrderListDto;
import com.baibei.shiyi.trade.feign.bean.vo.CustomerEntrustOrderListVo;
import com.baibei.shiyi.trade.service.IEntrustOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: hyc
 * @date: 2019/11/14 14:41
 * @description:
 */
@RestController
public class AdminEntrustOrderController implements IAdminEntrustOrderBase {

    @Autowired
    private IEntrustOrderService entrustOrderService;
    @Override
    public ApiResult<MyPageInfo<CustomerEntrustOrderListVo>> pageList(@RequestBody  CustomerEntrustOrderListDto customerEntrustOrderListDto) {
        return ApiResult.success(entrustOrderService.mypageList(customerEntrustOrderListDto));
    }

    @Override
    public ApiResult<List<CustomerEntrustOrderListVo>> export(@RequestBody  CustomerEntrustOrderListDto customerEntrustOrderListDto) {
        return ApiResult.success(entrustOrderService.export(customerEntrustOrderListDto));
    }
}
