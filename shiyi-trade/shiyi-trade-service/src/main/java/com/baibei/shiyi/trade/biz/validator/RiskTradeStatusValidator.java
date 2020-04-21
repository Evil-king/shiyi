package com.baibei.shiyi.trade.biz.validator;

import com.baibei.shiyi.common.tool.validator2.ValidateException;
import com.baibei.shiyi.common.tool.validator2.Validator;
import com.baibei.shiyi.common.tool.validator2.ValidatorContext;
import com.baibei.shiyi.trade.service.IRiskTradeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/12/25 16:11
 * @description: 系统临时停盘风控
 */
@Component
@Slf4j
public class RiskTradeStatusValidator implements Validator {
    @Autowired
    private IRiskTradeService riskTradeService;

    @Override
    public void validate(ValidatorContext context, Object o) {
        boolean flag = riskTradeService.isRiskTrade();
        if (flag) {
            throw new ValidateException("停盘中");
        }
    }
}