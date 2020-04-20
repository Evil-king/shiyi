package com.baibei.shiyi.cash.feign.client.shiyi;

import com.baibei.shiyi.cash.feign.base.dto.PABDepositDto;
import com.baibei.shiyi.cash.feign.base.vo.PABDepositVo;
import com.baibei.shiyi.cash.feign.client.hystrix.DepositHystrix;
import com.baibei.shiyi.common.tool.api.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "${shiyi-cash:shiyi-cash}", path = "/shiyi/cash/order",fallbackFactory = DepositHystrix.class)
public interface IShiyiDepositFeign {

    @PostMapping(path = "/deposit")
    ApiResult<PABDepositVo> deposit(@Validated @RequestBody PABDepositDto pabDepositDto);
}
