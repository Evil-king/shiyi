package com.baibei.shiyi.trade.service.impl;

import com.baibei.shiyi.account.feign.bean.dto.ChangeAmountDto;
import com.baibei.shiyi.account.feign.client.AccountFeign;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.enumeration.FreezingAmountTradeTypeEnum;
import com.baibei.shiyi.common.tool.exception.SystemException;
import com.baibei.shiyi.trade.model.EntrustOrder;
import com.baibei.shiyi.trade.service.IEntrustOrderService;
import com.baibei.shiyi.trade.service.IHoldPostionChangeService;
import com.baibei.shiyi.trade.service.IRevokeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * @author: 会跳舞的机器人
 * @date: 2020/1/8 17:25
 * @description:
 */
@Service
@Slf4j
public class RevokeServiceImpl implements IRevokeService {
    @Autowired
    private IEntrustOrderService entrustOrderService;
    @Autowired
    private IHoldPostionChangeService holdPostionChangeService;
    @Autowired
    private AccountFeign accountFeign;

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void revokeBuyOrder(EntrustOrder entrustOrder, String result) {
        // step1.更新委托单状态
        entrustOrderService.revoke(entrustOrder, result);
        // step2.解冻客户资金
        BigDecimal cost = entrustOrder.getPrice().multiply(new BigDecimal(entrustOrder.getWaitCount()));
        BigDecimal fee = entrustOrder.getBuyFee().multiply(new BigDecimal(entrustOrder.getWaitCount()));
        BigDecimal changeAmount = cost.add(fee);
        ChangeAmountDto changeAmountDto = new ChangeAmountDto();
        changeAmountDto.setCustomerNo(entrustOrder.getCustomerNo());
        changeAmountDto.setChangeAmount(changeAmount);
        changeAmountDto.setOrderNo(entrustOrder.getEntrustNo());
        changeAmountDto.setTradeType(FreezingAmountTradeTypeEnum.LIST_REVOKE.getCode());
        changeAmountDto.setReType(Constants.Retype.IN);
        ApiResult apiResult = accountFeign.frozenAmount(changeAmountDto);
        if (apiResult.hasFail()) {
            log.info("委托单【{}】撤单解冻资金失败，apiResult={}", entrustOrder.getEntrustNo(), apiResult);
            throw new SystemException("委托单撤单解冻失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void revokeSellOrder(EntrustOrder entrustOrder, String result) {
        // step1.更新委托单状态
        entrustOrderService.revoke(entrustOrder, result);
        // step2.解冻客户持仓
        holdPostionChangeService.unfreeze(entrustOrder.getCustomerNo(), entrustOrder.getProductTradeNo(), entrustOrder.getWaitCount());
    }
}