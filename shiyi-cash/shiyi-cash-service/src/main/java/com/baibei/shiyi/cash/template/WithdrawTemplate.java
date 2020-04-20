package com.baibei.shiyi.cash.template;

import com.baibei.shiyi.cash.feign.base.dto.OrderWithdrawDto;
import com.baibei.shiyi.cash.model.OrderWithdraw;
import com.baibei.shiyi.cash.service.IValidateService;
import com.baibei.shiyi.common.tool.api.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author hwq
 * @date 2019/06/06
 */
@Component
public abstract class WithdrawTemplate {

    @Autowired
    private IValidateService validateService;


    public ApiResult doProcess(OrderWithdrawDto orderWithdrawDto) {
        /*ApiResult order = doBeforeProcess(orderWithdrawDto);
        return order;*/
        return null;
    }

    /**
     * 前置处理
     * <p>
     * 处理验证、创建订单
     *
     * @param orderWithdrawDto
     */
    protected void doBeforeProcess(OrderWithdrawDto orderWithdrawDto) {
        /*validateService.validateWithdraw(orderWithdrawDto);
        OrderWithdraw order = validateService.createOrder(orderWithdrawDto);*/
    }
}
