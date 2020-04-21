package com.baibei.shiyi.trade.web.api;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.trade.common.dto.TradeProductPicDto;
import com.baibei.shiyi.trade.common.vo.TradeProductSlideListVo;
import com.baibei.shiyi.trade.service.IProductPicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/trade/tradeProduct")
public class ApiTradeProductController {

    @Autowired
    private IProductPicService productPicService;

    @RequestMapping("/getTradeProductPic")
    public ApiResult<TradeProductSlideListVo> getTradeProductPic(@RequestBody TradeProductPicDto productPicDto){
        return productPicService.getTradeProductPic(productPicDto);
    }

}
