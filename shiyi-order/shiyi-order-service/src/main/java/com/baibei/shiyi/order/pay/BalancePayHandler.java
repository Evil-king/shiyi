package com.baibei.shiyi.order.pay;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.order.common.dto.PayDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/9/16 11:47
 * @description: 余额支付
 */
@Component
@Slf4j
public class BalancePayHandler implements PayInterface {


    @Override
    public ApiResult pay(PayDto payDto) {
        log.info("订单号为{}，支付方式为余额支付", payDto.getOrderNo());

        return ApiResult.success();
    }
}