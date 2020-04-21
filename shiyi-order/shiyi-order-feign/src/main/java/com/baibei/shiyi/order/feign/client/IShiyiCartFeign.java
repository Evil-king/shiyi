package com.baibei.shiyi.order.feign.client;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.order.feign.base.dto.ShiyiCartDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "${shiyi-order:shiyi-order}", path = "/shiyi/cart")
public interface IShiyiCartFeign {

    @PostMapping("/deleteCardProduct")
    ApiResult deleteCardProduct(@RequestBody ShiyiCartDto shiyiCardDto);

}
