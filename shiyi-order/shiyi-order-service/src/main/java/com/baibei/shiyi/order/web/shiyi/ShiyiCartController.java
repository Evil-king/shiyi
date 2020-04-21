package com.baibei.shiyi.order.web.shiyi;

import com.alibaba.fastjson.JSONObject;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.order.feign.base.dto.ShiyiCartDto;
import com.baibei.shiyi.order.feign.client.IShiyiCartFeign;
import com.baibei.shiyi.order.service.ICartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/5/24 9:49 AM
 * @description:
 */
@RestController
@RequestMapping("/shiyi/cart")
@Slf4j
public class ShiyiCartController implements IShiyiCartFeign {

    @Autowired
    private ICartService cartService;

    @Override
    public ApiResult deleteCardProduct(@Validated @RequestBody ShiyiCartDto shiyiCardDto) {
        // 1、过滤掉已经被用户移除购物车的商品
        log.info("当前移除购物车信息为{}", JSONObject.toJSONString(shiyiCardDto));
        List<String> cartIds = cartService.findByCart(shiyiCardDto.getCartIds())
                .stream().map(result -> result.getId().toString()).collect(Collectors.toList());
        if (cartIds != null || !cartIds.isEmpty()) {
            cartService.batchDelete(cartIds);
        }
        return ApiResult.success();
    }
}
