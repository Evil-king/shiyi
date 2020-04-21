package com.baibei.shiyi.trade.feign.client.hystrix;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.trade.feign.bean.dto.ChangeHoldPositionDto;
import com.baibei.shiyi.trade.feign.bean.dto.ProductDto;
import com.baibei.shiyi.trade.feign.bean.vo.ProductVo;
import com.baibei.shiyi.trade.feign.client.shiyi.TradeFeign;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: 会跳舞的机器人
 * @date: 2020/1/3 15:05
 * @description:
 */
@Slf4j
@Component
public class TradeHystrix implements FallbackFactory<TradeFeign> {
    @Override
    public TradeFeign create(Throwable throwable) {
        return new TradeFeign() {
            @Override
            public ApiResult<List<ProductVo>> productList(ProductDto dto) {
                log.info("productList fallback，reason was {}", throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult changeHoldPosition(List<ChangeHoldPositionDto> changeHoldPositionDtoList) {
                log.info("changeHoldPosition fallback，reason was {}", throwable);
                return ApiResult.serviceFail();
            }
        };
    }
}