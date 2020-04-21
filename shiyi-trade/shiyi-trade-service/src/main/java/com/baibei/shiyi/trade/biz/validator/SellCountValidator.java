package com.baibei.shiyi.trade.biz.validator;

import com.baibei.shiyi.common.tool.exception.SystemException;
import com.baibei.shiyi.common.tool.validator2.ValidateException;
import com.baibei.shiyi.common.tool.validator2.Validator;
import com.baibei.shiyi.common.tool.validator2.ValidatorContext;
import com.baibei.shiyi.trade.common.bo.TradeValidateBo;
import com.baibei.shiyi.trade.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/12/26 20:21
 * @description: 卖出数量是否合法验证器
 */
@Slf4j
@Component
public class SellCountValidator implements Validator<TradeValidateBo> {
    @Override
    public void validate(ValidatorContext context, TradeValidateBo tradeValidateBo) {
        Product product = context.getClazz("product");
        if (product == null) {
            throw new SystemException("上下文中获取交易商品为空");
        }
        Integer count = tradeValidateBo.getCount();
        if (product.getMaxTrade() != null && product.getMaxTrade() < count) {
            throw new ValidateException(String.format("超过单笔最大可卖数[%s]", product.getMaxTrade()));
        }
    }
}