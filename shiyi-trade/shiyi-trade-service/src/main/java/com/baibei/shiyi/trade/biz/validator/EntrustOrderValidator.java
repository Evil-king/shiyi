package com.baibei.shiyi.trade.biz.validator;

import com.baibei.shiyi.common.tool.api.ResultEnum;
import com.baibei.shiyi.common.tool.enumeration.EntrustOrderResultEnum;
import com.baibei.shiyi.common.tool.validator2.ValidateException;
import com.baibei.shiyi.common.tool.validator2.Validator;
import com.baibei.shiyi.common.tool.validator2.ValidatorContext;
import com.baibei.shiyi.trade.common.bo.TradeValidateBo;
import com.baibei.shiyi.trade.model.EntrustOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/12/30 18:16
 * @description: 委托单验证器
 */
@Component
@Slf4j
public class EntrustOrderValidator implements Validator<TradeValidateBo> {
    @Override
    public void validate(ValidatorContext context, TradeValidateBo tradeValidateBo) {
        // 判断委托单状态
        EntrustOrder entrustOrder = tradeValidateBo.getEntrustOrder();
        if (!EntrustOrderResultEnum.WAIT_DEAL.getCode().equals(entrustOrder.getResult())) {
            throw new ValidateException(ResultEnum.DELIST_DEAL);
        }
        // 判断摘牌数量
        if (tradeValidateBo.getCount() > entrustOrder.getWaitCount()) {
            throw new ValidateException(ResultEnum.DELIST_DEAL);
        }
        if (tradeValidateBo.getCustomerNo().equals(entrustOrder.getCustomerNo())) {
            throw new ValidateException("不可摘自身的挂单！");
        }
    }
}