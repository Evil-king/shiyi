package com.baibei.shiyi.trade.service.impl;

import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.exception.SystemException;
import com.baibei.shiyi.common.tool.utils.DateUtil;
import com.baibei.shiyi.common.tool.utils.IdWorker;
import com.baibei.shiyi.trade.common.bo.ChangeHoldPositionBo;
import com.baibei.shiyi.trade.model.HoldDetails;
import com.baibei.shiyi.trade.model.HoldPosition;
import com.baibei.shiyi.trade.model.TradeConfig;
import com.baibei.shiyi.trade.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import tk.mybatis.mapper.entity.Condition;

import java.util.Date;
import java.util.List;

/**
 * @author: 会跳舞的机器人
 * @date: 2020/1/11 13:45
 * @description:
 */
@Service
@Slf4j
@Transactional(rollbackFor = Throwable.class)
public class HoldPositionChangeServiceImpl implements IHoldPostionChangeService {
    @Autowired
    private IHoldRecordService holdRecordService;
    @Autowired
    private IHoldDetailsService holdDetailsService;
    @Autowired
    private IHoldPositionService holdPositionService;
    @Autowired
    private ITradeConfigService tradeConfigService;

    @Override
    public void frozen(String customerNo, String productTradeNo, Integer count) {
        HoldPosition holdPosition = holdPositionService.find(customerNo, productTradeNo);
        Assert.notNull(holdPosition, "客户持仓信息为空");
        if (holdPosition.getCanSellCount() - count < 0) {
            log.info("可卖数量不足，canSellCount={},count={}", holdPosition.getCanSellCount(), count);
            throw new SystemException("可卖数量不足");
        }
        HoldPosition newPosition = new HoldPosition();
        newPosition.setCanSellCount(holdPosition.getCanSellCount() - count);
        newPosition.setFrozenCount(holdPosition.getFrozenCount() + count);
        newPosition.setModifyTime(new Date());
        newPosition.setVersion(holdPosition.getVersion() + 1);
        Condition condition = new Condition(HoldPosition.class);
        condition.createCriteria().andEqualTo("id", holdPosition.getId())
                .andEqualTo("version", holdPosition.getVersion());
        boolean flag = holdPositionService.updateByConditionSelective(newPosition, condition);
        if (!flag) {
            throw new SystemException("冻结客户持仓失败");
        }
    }

    @Override
    public void unfreeze(String customerNo, String productTradeNo, Integer count) {
        HoldPosition holdPosition = holdPositionService.find(customerNo, productTradeNo);
        Assert.notNull(holdPosition, "客户持仓信息为空");
        if (holdPosition.getFrozenCount() - count < 0) {
            log.info("冻结数量不足，frozenCount={},count={}", holdPosition.getFrozenCount(), count);
            throw new SystemException("冻结数量不足");
        }
        HoldPosition newPosition = new HoldPosition();
        newPosition.setCanSellCount(holdPosition.getCanSellCount() + count);
        newPosition.setFrozenCount(holdPosition.getFrozenCount() - count);
        newPosition.setModifyTime(new Date());
        newPosition.setVersion(holdPosition.getVersion() + 1);
        Condition condition = new Condition(HoldPosition.class);
        condition.createCriteria().andEqualTo("id", holdPosition.getId())
                .andEqualTo("version", holdPosition.getVersion());
        boolean flag = holdPositionService.updateByConditionSelective(newPosition, condition);
        if (!flag) {
            throw new SystemException("解冻客户持仓失败");
        }
    }

    @Override
    public void unfreezeAndDeduct(ChangeHoldPositionBo bo) {
        // step1.参数校验
        changeValidate(bo);
        HoldPosition holdPosition = holdPositionService.find(bo.getCustomerNo(), bo.getProductTradeNo());
        Assert.notNull(holdPosition, "客户持仓信息为空");
        Integer count = bo.getCount();
        if (holdPosition.getRemaindCount() - count < 0) {
            log.info("持仓数量不足，remaindCount={},count={}", holdPosition.getRemaindCount(), count);
            throw new SystemException("持仓数量不足");
        }
        if (holdPosition.getFrozenCount() - count < 0) {
            log.info("冻结数量不足，frozenCount={},count={}", holdPosition.getFrozenCount(), count);
            throw new SystemException("冻结数量不足");
        }
        // step2.扣减剩余数、冻结数
        HoldPosition newPosition = new HoldPosition();
        newPosition.setRemaindCount(holdPosition.getRemaindCount() - count);
        newPosition.setFrozenCount(holdPosition.getFrozenCount() - count);
        newPosition.setModifyTime(new Date());
        newPosition.setVersion(holdPosition.getVersion() + 1);
        Condition condition = new Condition(HoldPosition.class);
        condition.createCriteria().andEqualTo("id", holdPosition.getId())
                .andEqualTo("version", holdPosition.getVersion());
        boolean flag = holdPositionService.updateByConditionSelective(newPosition, condition);
        if (!flag) {
            throw new SystemException("扣除客户持仓失败");
        }
        // step3.持仓变动记录
        holdRecordService.saveRecord(bo);
    }

    @Override
    public void deduct(ChangeHoldPositionBo bo) {
        // step1.参数校验
        changeValidate(bo);
        HoldPosition holdPosition = holdPositionService.find(bo.getCustomerNo(), bo.getProductTradeNo());
        Assert.notNull(holdPosition, "客户持仓信息为空");
        Integer count = bo.getCount();
        if (holdPosition.getRemaindCount() - count < 0) {
            log.info("持仓数量不足，remaindCount={},count={}", holdPosition.getRemaindCount(), count);
            throw new SystemException("持仓数量不足");
        }
        if (holdPosition.getCanSellCount() - count < 0) {
            log.info("可卖数量不足，canSellCount={},count={}", holdPosition.getCanSellCount(), count);
            throw new SystemException("可卖数量不足");
        }
        // step2.扣减持仓数
        HoldPosition newPosition = new HoldPosition();
        newPosition.setRemaindCount(holdPosition.getRemaindCount() - count);
        newPosition.setCanSellCount(holdPosition.getCanSellCount() - count);
        newPosition.setModifyTime(new Date());
        newPosition.setVersion(holdPosition.getVersion() + 1);
        Condition condition = new Condition(HoldPosition.class);
        condition.createCriteria().andEqualTo("id", holdPosition.getId())
                .andEqualTo("version", holdPosition.getVersion());
        boolean flag = holdPositionService.updateByConditionSelective(newPosition, condition);
        if (!flag) {
            throw new SystemException("扣除客户持仓失败");
        }
        // step3.持仓变动记录
        holdRecordService.saveRecord(bo);
    }

    @Override
    public void increase(ChangeHoldPositionBo bo) {
        // step1.参数校验
        changeValidate(bo);
        Integer count = bo.getCount();
        HoldPosition holdPosition = holdPositionService.find(bo.getCustomerNo(), bo.getProductTradeNo());
        // step2.增客户持仓，不存在则创建
        if (holdPosition == null) {
            holdPosition = new HoldPosition();
            holdPosition.setId(IdWorker.getId());
            holdPosition.setCustomerNo(bo.getCustomerNo());
            holdPosition.setCustomerName(bo.getCustomerName());
            holdPosition.setProductTradeNo(bo.getProductTradeNo());
            holdPosition.setRemaindCount(count);
            holdPosition.setLockCount(count);
            holdPosition.setCanSellCount(0);
            holdPosition.setCreateTime(new Date());
            holdPosition.setVersion(0);
            holdPositionService.save(holdPosition);
        } else {
            HoldPosition newPosition = new HoldPosition();
            newPosition.setRemaindCount(holdPosition.getRemaindCount() + count);
            newPosition.setLockCount(holdPosition.getLockCount() + count);
            newPosition.setModifyTime(new Date());
            newPosition.setVersion(holdPosition.getVersion() + 1);
            Condition condition = new Condition(HoldPosition.class);
            condition.createCriteria().andEqualTo("id", holdPosition.getId())
                    .andEqualTo("version", holdPosition.getVersion());
            boolean flag = holdPositionService.updateByConditionSelective(newPosition, condition);
            if (!flag) {
                throw new SystemException("增加客户持仓失败");
            }
        }
        // step3.创建持仓明细
        holdDetailsService.save(bo);
        // step4.持仓变动记录
        holdRecordService.saveRecord(bo);
    }

    @Override
    public void unlock(HoldDetails details) {
        // 增加客户持仓
        String customerNo = details.getCustomerNo();
        String productTradeNo = details.getProductTradeNo();
        Integer count = details.getCount();
        HoldPosition holdPosition = holdPositionService.find(customerNo, productTradeNo);
        if (holdPosition == null) {
            throw new SystemException("客户持仓记录不存在");
        }
        if (holdPosition.getLockCount() - count < 0) {
            log.info("锁仓数量不足，lockCount={},count={}", holdPosition.getLockCount(), count);
            throw new SystemException("锁仓数量不足");
        }
        HoldPosition newPosition = new HoldPosition();
        newPosition.setCanSellCount(holdPosition.getCanSellCount() + count);
        newPosition.setLockCount(holdPosition.getLockCount() - count);
        newPosition.setModifyTime(new Date());
        newPosition.setVersion(holdPosition.getVersion() + 1);
        Condition condition = new Condition(HoldPosition.class);
        condition.createCriteria().andEqualTo("id", holdPosition.getId())
                .andEqualTo("version", holdPosition.getVersion());
        boolean flag = holdPositionService.updateByConditionSelective(newPosition, condition);
        if (!flag) {
            throw new SystemException("解锁持仓失败");
        }
        // 修改解锁状态
        details.setModifyTime(new Date());
        details.setUnlockFlag(Byte.valueOf("1"));
        holdDetailsService.update(details);
    }

    @Override
    public void changeHoldPosition(List<ChangeHoldPositionBo> boList) {
        TradeConfig tradeConfig = tradeConfigService.findFromCache();
        if (tradeConfig == null) {
            throw new SystemException("交易配置不存在");
        }
        for (ChangeHoldPositionBo bo : boList) {
            bo.setTradeTime(DateUtil.addDay(tradeConfig.getExchangeFrozenDay()));
            if (Constants.Retype.IN.equals(bo.getReType())) {
                increase(bo);
            } else if (Constants.Retype.OUT.equals(bo.getReType())) {
                deduct(bo);
            } else {
                throw new IllegalArgumentException("reType参数错误");
            }
        }
    }


    /**
     * 参数校验
     *
     * @param bo
     */
    private void changeValidate(ChangeHoldPositionBo bo) {
        Assert.hasText(bo.getCustomerNo(), "customerNo不能为空");
        Assert.hasText(bo.getProductTradeNo(), "productTradeNo不能为空");
        Assert.notNull(bo.getCount(), "count不能为空");
        Assert.hasText(bo.getResource(), "resource不能为空");
        Assert.hasText(bo.getResourceNo(), "resourceNo不能为空");
        if (bo.getCount() <= 0) {
            throw new IllegalArgumentException("非法的参数>>>count");
        }
    }
}