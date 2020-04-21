package com.baibei.shiyi.trade.feign.client.admin;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.trade.feign.bean.dto.RiskTradeDto;
import com.baibei.shiyi.trade.feign.client.hystrix.AdminRiskTradeHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "${shiyi-trade:shiyi-trade}", path = "/shiyi/admin/risk/trade",fallbackFactory = AdminRiskTradeHystrix.class)
public interface IAdminRiskTradeFeign {

    @PostMapping(path = "/save")
    ApiResult save(@Validated @RequestBody RiskTradeDto riskTradeDto);

    @PostMapping(path = "/get")
    ApiResult get();
}
