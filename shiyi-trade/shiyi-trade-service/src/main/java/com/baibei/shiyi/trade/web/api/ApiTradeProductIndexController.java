package com.baibei.shiyi.trade.web.api;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.trade.common.vo.TradeIndexVo;
import com.baibei.shiyi.trade.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/trade/tradeProduct")
public class ApiTradeProductIndexController {

    @Autowired
    private IProductService productService;


    @RequestMapping("/index")
    public ApiResult<List<TradeIndexVo>> getIndex(){
        return productService.getIndex();
    }
}
