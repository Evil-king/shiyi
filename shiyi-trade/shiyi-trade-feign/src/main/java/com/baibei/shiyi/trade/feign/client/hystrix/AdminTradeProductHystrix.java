package com.baibei.shiyi.trade.feign.client.hystrix;

import com.alibaba.fastjson.JSONObject;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.trade.feign.base.IAdminTradeProductBase;
import com.baibei.shiyi.trade.feign.bean.dto.TradeProductAddDto;
import com.baibei.shiyi.trade.feign.bean.dto.TradeProductDto;
import com.baibei.shiyi.trade.feign.bean.dto.TradeProductLookDto;
import com.baibei.shiyi.trade.feign.bean.vo.TradeProductListVo;
import com.baibei.shiyi.trade.feign.bean.vo.TradeProductVo;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AdminTradeProductHystrix implements FallbackFactory<IAdminTradeProductBase> {
    @Override
    public IAdminTradeProductBase create(Throwable throwable) {
        return new IAdminTradeProductBase() {
            @Override
            public ApiResult<MyPageInfo<TradeProductListVo>> listPage(TradeProductDto tradeProductDto) {
                log.info("save is hystrix exception is {},Params is {}", throwable, JSONObject.toJSONString(tradeProductDto));
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult add(TradeProductAddDto tradeProductAddDto) {
                log.info("save is hystrix exception is {},Params is {}", throwable, JSONObject.toJSONString(tradeProductAddDto));
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult editOperator(TradeProductAddDto tradeProductAddDto) {
                log.info("save is hystrix exception is {},Params is {}", throwable, JSONObject.toJSONString(tradeProductAddDto));
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<TradeProductVo> look(TradeProductLookDto tradeProductLookDto) {
                log.info("save is hystrix exception is {},Params is {}", throwable, JSONObject.toJSONString(tradeProductLookDto));
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult modifyStatus() {
                log.info("save is hystrix exception is {},Params is {}", throwable, JSONObject.toJSONString(""));
                return ApiResult.serviceFail();
            }
        };
    }
}
