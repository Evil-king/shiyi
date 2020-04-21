package com.baibei.shiyi.trade.biz.validator;

import com.baibei.shiyi.common.tool.enumeration.CustomerStatusEnum;
import com.baibei.shiyi.common.tool.exception.SystemException;
import com.baibei.shiyi.common.tool.validator2.ValidateException;
import com.baibei.shiyi.common.tool.validator2.Validator;
import com.baibei.shiyi.common.tool.validator2.ValidatorContext;
import com.baibei.shiyi.user.feign.bean.vo.CustomerVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/12/26 14:10
 * @description: 客户买入交易状态验证器
 */
@Slf4j
@Component
public class BuyerTradeStatusValidator implements Validator {
    @Override
    public void validate(ValidatorContext context, Object o) {
        CustomerVo customerVo = context.getClazz("customerVo");
        if (customerVo == null) {
            throw new SystemException("客户信息不存在");
        }
        if (!StringUtils.isEmpty(customerVo.getCustomerStatus())
                && customerVo.getCustomerStatus().indexOf(CustomerStatusEnum.LIMIT_BUY.getCode()) != -1) {
            throw new ValidateException("买入失败，请联系客服");
        }
    }
}