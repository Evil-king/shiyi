package com.baibei.shiyi.trade.biz.validator;

import com.baibei.shiyi.common.tool.enumeration.CustomerStatusEnum;
import com.baibei.shiyi.common.tool.exception.SystemException;
import com.baibei.shiyi.common.tool.validator2.ValidateException;
import com.baibei.shiyi.common.tool.validator2.Validator;
import com.baibei.shiyi.common.tool.validator2.ValidatorContext;
import com.baibei.shiyi.user.feign.bean.vo.CustomerVo;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/12/26 14:10
 * @description: 客户卖出交易状态验证器
 */
@Component
public class SellerTradeStatusValidator implements Validator {
    @Override
    public void validate(ValidatorContext context, Object o) {
        CustomerVo customerVo = context.getClazz("customerVo");
        if (customerVo == null) {
            throw new SystemException("客户信息不存在");
        }
        if (!StringUtils.isEmpty(customerVo.getCustomerStatus())
                && customerVo.getCustomerStatus().indexOf(CustomerStatusEnum.LIMIT_SELL.getCode()) != -1) {
            throw new ValidateException("卖出失败，请联系客服");
        }
    }
}