package com.baibei.shiyi.trade.web.admin;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.trade.feign.base.IAdminDealOrderBase;
import com.baibei.shiyi.trade.feign.bean.dto.DealOrderDto;
import com.baibei.shiyi.trade.feign.bean.vo.DealOrderVo;
import com.baibei.shiyi.trade.service.IDealOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: hyc
 * @date: 2019/11/18 10:35
 * @description:
 */
@RestController
public class AdminDealOrderController implements IAdminDealOrderBase {
    @Autowired
    private IDealOrderService dealOrderService;
    @Override
    public ApiResult<MyPageInfo<DealOrderVo>> pageList(@RequestBody @Validated DealOrderDto dealOrderDto) {
        return ApiResult.success(dealOrderService.myPageList(dealOrderDto));
    }

    @Override
    public ApiResult<List<DealOrderVo>> List(@RequestBody DealOrderDto dealOrderDto) {
        return ApiResult.success(dealOrderService.List(dealOrderDto));
    }
}
