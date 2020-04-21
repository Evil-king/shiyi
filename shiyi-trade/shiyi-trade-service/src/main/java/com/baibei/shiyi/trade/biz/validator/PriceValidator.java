package com.baibei.shiyi.trade.biz.validator;

import com.baibei.shiyi.common.tool.exception.SystemException;
import com.baibei.shiyi.common.tool.validator2.ValidateException;
import com.baibei.shiyi.common.tool.validator2.Validator;
import com.baibei.shiyi.common.tool.validator2.ValidatorContext;
import com.baibei.shiyi.quotation.feign.bean.vo.QuoteVo;
import com.baibei.shiyi.quotation.feign.service.ICommonQuoteService;
import com.baibei.shiyi.trade.common.bo.TradeValidateBo;
import com.baibei.shiyi.trade.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author: 会跳舞的机器人
 * @date: 2020/1/10 17:08
 * @description: 报价是否合理
 */
@Component
public class PriceValidator implements Validator<TradeValidateBo> {
    private static final BigDecimal oneHundred = new BigDecimal("100");
    private static final BigDecimal one = new BigDecimal("1");
    @Autowired
    private ICommonQuoteService commonQuoteService;

    @Override
    public void validate(ValidatorContext context, TradeValidateBo bo) {
        QuoteVo quoteVo = commonQuoteService.getQuoteInfo(bo.getProductTradeNo());
        if (quoteVo == null) {
            throw new SystemException("获取行情报价为空");
        }
        Product product = context.getClazz("product");
        if (product == null) {
            throw new SystemException("上下文中获取交易商品为空");
        }
        BigDecimal yestPrice = quoteVo.getYestPrice();
        // 涨停价：昨日收盘价*（1+最高报价比例）
        BigDecimal increaseLimitPrice = yestPrice.multiply((one.add(divideOneHundred(product.getHighestQuotedPrice()))))
                .divide(BigDecimal.ONE,2,BigDecimal.ROUND_DOWN).setScale(2);
        // 跌停价：昨日收盘价*（1-最低报价比例）
        BigDecimal fallLimitPrice = yestPrice.multiply((one.subtract(divideOneHundred(product.getLowestQuotedPrice()))))
                .divide(BigDecimal.ONE,2,BigDecimal.ROUND_DOWN).setScale(2);
        if (bo.getPrice().compareTo(fallLimitPrice) < 0 || bo.getPrice().compareTo(increaseLimitPrice) > 0) {
            throw new ValidateException("请输入【跌停价】至【涨停价】之间的价格");
        }
    }

    private BigDecimal divideOneHundred(BigDecimal val) {
        return val.divide(oneHundred);
    }
}