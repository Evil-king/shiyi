package com.baibei.shiyi.trade.web.api;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.trade.biz.TradeBiz;
import com.baibei.shiyi.trade.common.dto.TradeDeListDto;
import com.baibei.shiyi.trade.common.dto.TradeListDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/12/24 14:21
 * @description:
 */
@RestController
@RequestMapping("/auth/api/trade")
public class AuthApiTradeController {
    @Autowired
    private TradeBiz tradeBiz;

    /**
     * 挂牌买入/卖出
     *
     * @param tradeListDto
     * @return
     */
    @PostMapping("/list")
    public ApiResult<String> list(@RequestBody @Validated TradeListDto tradeListDto) {
        if (Constants.TradeDirection.BUY.equals(tradeListDto.getDirection())) {
            return tradeBiz.listBuy(tradeListDto);
        } else if (Constants.TradeDirection.SELL.equals(tradeListDto.getDirection())) {
            return tradeBiz.listSell(tradeListDto);
        } else {
            return ApiResult.badParam("挂牌方向错误");
        }
    }

    /**
     * 摘牌买入/卖出
     *
     * @param tradeDeListDto
     * @return
     */
    @PostMapping("/delist")
    public ApiResult<String> delist(@RequestBody @Validated TradeDeListDto tradeDeListDto) {
        return tradeBiz.delist(tradeDeListDto);
    }
}