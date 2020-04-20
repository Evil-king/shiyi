package com.baibei.shiyi.cash.service;


import com.baibei.shiyi.cash.feign.base.dto.OrderWithdrawDto;
import com.baibei.shiyi.cash.model.OrderWithdraw;
import com.baibei.shiyi.common.tool.api.ApiResult;

import java.math.BigDecimal;

/**
 * @author hwq
 * @date 2019/06/06
 */
public interface IValidateService {

    /**
     * 出金请求相关校验
     * @param orderWithdrawDto
     * @return
     */
     void validateWithdraw(OrderWithdrawDto orderWithdrawDto);


}
