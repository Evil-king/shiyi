package com.baibei.shiyi.trade.biz.validator;

import com.baibei.shiyi.account.feign.bean.dto.CustomerNoDto;
import com.baibei.shiyi.account.feign.bean.vo.AccountVo;
import com.baibei.shiyi.account.feign.client.AccountFeign;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.exception.SystemException;
import com.baibei.shiyi.common.tool.validator2.ValidateException;
import com.baibei.shiyi.common.tool.validator2.Validator;
import com.baibei.shiyi.common.tool.validator2.ValidatorContext;
import com.baibei.shiyi.trade.common.bo.TradeValidateBo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/12/25 16:11
 * @description: 交易买入资金验证器
 */
@Component
@Slf4j
public class BuyerBalanceValidator implements Validator<TradeValidateBo> {
    @Autowired
    private AccountFeign accountFeign;

    @Override
    public void validate(ValidatorContext context, TradeValidateBo tradeValidateBo) {
        CustomerNoDto customerNoDto = new CustomerNoDto();
        customerNoDto.setCustomerNo(tradeValidateBo.getCustomerNo());
        ApiResult<AccountVo> apiResult = accountFeign.findAccount(customerNoDto);
        if (apiResult.hasFail()) {
            log.info("获取账户信息失败，apiResult={}", apiResult.toString());
            throw new SystemException("获取账户信息失败");
        }
        AccountVo accountVo = apiResult.getData();
        if (accountVo == null) {
            log.info("获取账户信息为空");
            throw new SystemException("获取账户信息失败");
        }
        if (accountVo.getBalance().compareTo(tradeValidateBo.getTotalCost()) < 0) {
            throw new ValidateException("买入失败，余额不足");
        }
    }
}