package com.baibei.shiyi.account.rocketmq.comsume;

import com.baibei.component.rocketmq.core.consumer.IConsumer;
import com.baibei.shiyi.account.feign.bean.dto.ChangeAmountDto;
import com.baibei.shiyi.account.service.IAccountService;
import com.baibei.shiyi.common.tool.api.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: Longer
 * @date: 2019/11/1 10:41
 * @description: 加钱的钱
 */
@Component
@Slf4j
public class WithdrawAddMoneyConsumerImpl implements IConsumer<ChangeAmountDto> {
    @Autowired
    private IAccountService accountService;

    @Override
    public ApiResult execute(ChangeAmountDto changeAmountDto) {
        ApiResult apiResult;
        try{
            accountService.updateWithDraw(changeAmountDto);
            apiResult=ApiResult.success();
        }catch (Exception e){
            log.error("AuditWithdrawConsumerImpl:出金加钱报错：",e);
            e.printStackTrace();
            apiResult=ApiResult.error();
        }
        return apiResult;
    }
}