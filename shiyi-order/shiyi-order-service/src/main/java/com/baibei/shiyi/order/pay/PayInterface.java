package com.baibei.shiyi.order.pay;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.order.common.dto.PayDto;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/9/16 11:44
 * @description:
 */
public interface PayInterface {

    ApiResult pay(PayDto payDto);

}