package com.baibei.shiyi.trade.feign.client.admin;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.trade.feign.bean.dto.AdminTradeConfigDto;
import com.baibei.shiyi.trade.feign.client.hystrix.AdminTradeConfigHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "${shiyi-trade:shiyi-trade}", path = "/shiyi/admin/trade/config",fallbackFactory = AdminTradeConfigHystrix.class)
public interface IAdminTradeConfigFeign {


    @PostMapping("/save")
    ApiResult save(@RequestBody AdminTradeConfigDto adminTradeConfigDto);

    @PostMapping("/getConfig")
    ApiResult<AdminTradeConfigDto> getConfig();

}
