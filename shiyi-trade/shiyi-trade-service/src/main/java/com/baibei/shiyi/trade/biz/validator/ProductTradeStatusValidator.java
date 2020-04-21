package com.baibei.shiyi.trade.biz.validator;

import com.baibei.shiyi.common.tool.enumeration.ProductTradeStatusEnum;
import com.baibei.shiyi.common.tool.exception.SystemException;
import com.baibei.shiyi.common.tool.validator2.ValidateException;
import com.baibei.shiyi.common.tool.validator2.Validator;
import com.baibei.shiyi.common.tool.validator2.ValidatorContext;
import com.baibei.shiyi.trade.common.bo.TradeValidateBo;
import com.baibei.shiyi.trade.model.Product;
import com.baibei.shiyi.trade.service.IProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/12/25 16:11
 * @description: 交易商品上市状态验证器
 */
@Slf4j
@Component
public class ProductTradeStatusValidator implements Validator<TradeValidateBo> {
    @Autowired
    private IProductService productService;

    @Override
    public void validate(ValidatorContext context, TradeValidateBo tradeValidateBo) {
        String productTradeNo = tradeValidateBo.getProductTradeNo();
        if (StringUtils.isEmpty(productTradeNo)) {
            throw new ValidateException("交易商品编码为空");
        }
        Product product = productService.findEffective(productTradeNo);
        if (product == null) {
            throw new SystemException("交易商品不存在");
        }
        if (StringUtils.isEmpty(product.getTradeStatus())
                || !ProductTradeStatusEnum.TRADING.getCode().equals(product.getTradeStatus())) {
            throw new ValidateException("商品未上市");
        }
        // 将交易商品信息存入上下文
        context.setAttribute("product", product);
    }
}