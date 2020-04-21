package com.baibei.shiyi.trade.web.api;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.trade.feign.bean.dto.DealOrderDto;
import com.baibei.shiyi.trade.feign.bean.vo.DealOrderVo;
import com.baibei.shiyi.trade.service.IDealOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/api/trade/dealOrder")
public class ApiDealOrderController {

    @Autowired
    private IDealOrderService dealOrderService;

    @RequestMapping("/pageList")
    public ApiResult<MyPageInfo<DealOrderVo>> pageList(@RequestBody @Validated DealOrderDto dealOrderDto) {
        return ApiResult.success(dealOrderService.myPageList(dealOrderDto));
    }
}
