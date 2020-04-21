package com.baibei.shiyi.trade.feign.client.shiyi;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.trade.feign.bean.dto.ChangeHoldPositionDto;
import com.baibei.shiyi.trade.feign.bean.dto.ProductDto;
import com.baibei.shiyi.trade.feign.bean.vo.ProductVo;
import com.baibei.shiyi.trade.feign.client.hystrix.TradeHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author: 会跳舞的机器人
 * @date: 2020/1/3 15:04
 * @description:
 */
@FeignClient(value = "shiyi-trade", fallbackFactory = TradeHystrix.class)
public interface TradeFeign {

    /**
     * 获取所有交易中的商品列表
     *
     * @param dto
     * @return
     */
    @PostMapping(value = "/shiyi/trade/productList")
    ApiResult<List<ProductVo>> productList(@RequestBody ProductDto dto);


    /**
     * 持仓变动
     *
     * @param changeHoldPositionDtoList
     * @return
     */
    @PostMapping(value = "/shiyi/trade/changeHoldPosition")
    ApiResult changeHoldPosition(@RequestBody List<ChangeHoldPositionDto> changeHoldPositionDtoList);
}