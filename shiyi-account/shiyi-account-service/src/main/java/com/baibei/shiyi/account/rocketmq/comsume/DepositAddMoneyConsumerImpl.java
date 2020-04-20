package com.baibei.shiyi.account.rocketmq.comsume;

import com.alibaba.fastjson.JSONObject;
import com.baibei.component.rocketmq.core.consumer.IConsumer;
import com.baibei.shiyi.account.feign.bean.dto.ChangeAmountDto;
import com.baibei.shiyi.account.service.IAccountService;
import com.baibei.shiyi.common.tool.api.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 入金的消息实际消费
 */
@Component
@Slf4j
public class DepositAddMoneyConsumerImpl implements IConsumer<ChangeAmountDto> {

    @Autowired
    private IAccountService accountService;

    @Override
    public ApiResult execute(ChangeAmountDto changeAmountDto) {
        log.info("-------开始入金,当前的入金消息为{}------", JSONObject.toJSONString(changeAmountDto));
        ApiResult result;
        try {
            accountService.updateWithDraw(changeAmountDto);
            result = ApiResult.success();
        } catch (Exception ex) {
            log.error("入金加钱报错", ex);
            ex.printStackTrace();
            result = ApiResult.error();
        }
        return result;
    }
}
