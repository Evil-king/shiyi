package com.baibei.shiyi.trade.biz.validator;

import com.baibei.shiyi.common.tool.validator2.ValidateException;
import com.baibei.shiyi.common.tool.validator2.Validator;
import com.baibei.shiyi.common.tool.validator2.ValidatorContext;
import com.baibei.shiyi.trade.service.ITradeDayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/12/25 16:11
 * @description: 交易时间校验器
 */
@Component
public class TradeTimeValidator implements Validator {
    @Autowired
    private ITradeDayService tradeDayService;

    @Override
    public void validate(ValidatorContext context, Object o) {
        // 判断交易日
        boolean tradeDayFlag = tradeDayService.isTradeDay();
        if (!tradeDayFlag) {
            throw new ValidateException("非交易时间");
        }
        // 判断交易时间
        boolean tradeTimeFlag = tradeDayService.isTradeTime();
        if (!tradeTimeFlag) {
            throw new ValidateException("非交易时间");
        }
    }
}