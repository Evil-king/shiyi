package com.baibei.shiyi.trade.web.api;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.trade.common.vo.TradeGlobalConfigVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: 会跳舞的机器人
 * @date: 2020/1/7 16:48
 * @description:
 */
@RestController
@RequestMapping("/api/trade")
public class ApiGlobalConfigController {
    @Value("${trade.pic}")
    private String tradePic;

    @Value("${trade.productTradeNo}")
    private String productTradeNo;

    /**
     * 交易全局配置
     *
     * @return
     */
    @PostMapping("/globalConfig")
    public ApiResult<TradeGlobalConfigVo> globalConfig() {
        TradeGlobalConfigVo vo = new TradeGlobalConfigVo();
        vo.setTradePic(tradePic);
        vo.setProductTradeNo(productTradeNo);
        return ApiResult.success(vo);
    }
}