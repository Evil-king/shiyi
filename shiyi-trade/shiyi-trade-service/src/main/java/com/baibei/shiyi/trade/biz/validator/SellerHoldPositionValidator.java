package com.baibei.shiyi.trade.biz.validator;

import com.baibei.shiyi.common.tool.validator2.ValidateException;
import com.baibei.shiyi.common.tool.validator2.Validator;
import com.baibei.shiyi.common.tool.validator2.ValidatorContext;
import com.baibei.shiyi.trade.common.bo.TradeValidateBo;
import com.baibei.shiyi.trade.model.HoldPosition;
import com.baibei.shiyi.trade.service.IHoldPositionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/12/27 14:09
 * @description: 卖出持仓单数量验证器
 */
@Slf4j
@Component
public class SellerHoldPositionValidator implements Validator<TradeValidateBo> {
    @Autowired
    private IHoldPositionService holdPositionService;

    @Override
    public void validate(ValidatorContext context, TradeValidateBo tradeValidateBo) {
        String productTradeNo = tradeValidateBo.getProductTradeNo();
        Integer count = tradeValidateBo.getCount();
        String customerNo = tradeValidateBo.getCustomerNo();
        HoldPosition holdPosition = holdPositionService.find(customerNo, productTradeNo);
        if (holdPosition == null) {
            log.info("客户{}持仓商品{}数据为空", customerNo, productTradeNo);
            throw new ValidateException("卖出失败，可卖数量不足");
        }
        if (holdPosition.getCanSellCount() < count) {
            throw new ValidateException("卖出失败，可卖数量不足");
        }
    }
}