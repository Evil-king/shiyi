package com.baibei.shiyi.trade.web.api;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.trade.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/trade/tradeProduct")
public class ApiTradeProductStatusController {

    @Autowired
    private IProductService productService;

    @RequestMapping("/modifyStatus")
    public ApiResult modifyStatus(){
        productService.modifyStatus();
        return ApiResult.success();
    }
}
