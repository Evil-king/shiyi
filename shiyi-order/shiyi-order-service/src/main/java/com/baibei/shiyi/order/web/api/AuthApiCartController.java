package com.baibei.shiyi.order.web.api;

import com.baibei.shiyi.common.core.aop.NoRepeatSubmit;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.order.common.dto.*;
import com.baibei.shiyi.order.common.vo.CartListVo;
import com.baibei.shiyi.order.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/8/1 10:10
 * @description: 购物车
 */
@RestController
@RequestMapping("/auth/api/order/cart")
public class AuthApiCartController {
    @Autowired
    private ICartService cartService;

    /**
     * 加入购物车
     *
     * @return
     */
    @PostMapping("/add")
    @NoRepeatSubmit
    public ApiResult<String> addCart(@RequestBody @Validated CartAddDto cardAddDto) {
        cartService.add(cardAddDto);
        return ApiResult.success("加入购物车成功");
    }

    /**
     * 购物车查询列表
     *
     * @return
     */
    @PostMapping("/list")
    public ApiResult<List<CartListVo>> list(@RequestBody @Validated CartListDto cartListDto) {
        return ApiResult.success(cartService.list(cartListDto));
    }


    /**
     * 购物车结算校验
     *
     * @return
     */
    @PostMapping("/cartValidate")
    public ApiResult<List<CartListVo>> cartValidate(@RequestBody @Validated CartValidateDto cartValidateDto) {
        return cartService.cartValidate(cartValidateDto);
    }

    /**
     * 更新购物车规格/数量
     *
     * @return
     */
    @PostMapping("/update")
    public ApiResult<String> update(@RequestBody @Validated CartUpdateDto cartUpdateDto) {
        cartService.update(cartUpdateDto);
        return ApiResult.success("更新购物车成功");
    }

    /**
     * 从购物车删除
     *
     * @return
     */
    @PostMapping("/delete")
    public ApiResult<String> delete(@RequestBody @Validated CartDeleteDto cartDeleteDto) {
        cartService.delete(cartDeleteDto.getIds());
        return ApiResult.success("删除成功");
    }
}