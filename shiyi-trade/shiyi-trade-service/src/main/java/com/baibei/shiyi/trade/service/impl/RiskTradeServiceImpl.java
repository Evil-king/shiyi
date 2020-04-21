package com.baibei.shiyi.trade.service.impl;

import com.baibei.shiyi.common.core.mybatis.AbstractService;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.utils.BeanUtil;
import com.baibei.shiyi.common.tool.utils.CollectionUtils;
import com.baibei.shiyi.common.tool.utils.DateUtil;
import com.baibei.shiyi.common.tool.utils.IdWorker;
import com.baibei.shiyi.trade.dao.RiskTradeMapper;
import com.baibei.shiyi.trade.feign.bean.dto.RiskTradeDto;
import com.baibei.shiyi.trade.model.RiskTrade;
import com.baibei.shiyi.trade.service.IRiskTradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author: uqing
 * @date: 2019/12/30 14:09:56
 * @description: RiskTrade服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RiskTradeServiceImpl extends AbstractService<RiskTrade> implements IRiskTradeService {
    @Autowired
    private RiskTradeMapper tblTraRiskTradeMapper;

    // 保存风控管理
    @Override
    public void saveRiskTrade(RiskTradeDto riskTradeDto) {
        //step 1 设置上一条失效
        Condition condition = new Condition(RiskTrade.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("flag", Constants.Flag.VALID);
        List<RiskTrade> riskTradeList = this.findByCondition(condition);
        if (!CollectionUtils.isEmpty(riskTradeList)) {
            List<String> riskTrades = riskTradeList.stream().map(result -> result.getId().toString()).collect(Collectors.toList());
            RiskTrade risk = new RiskTrade();
            risk.setFlag(new Byte(Constants.Flag.UNVALID));
            this.batchUpdate(riskTrades, risk);
        }
        // step2 保存数据
        RiskTrade riskTrade = new RiskTrade();
        riskTrade.setCreateTime(new Date());
        riskTrade.setId(IdWorker.getId());
        riskTrade.setEffectTime(new Date());
        riskTrade.setFlag(new Byte(Constants.Flag.VALID));
        riskTrade.setOpenFlag(new Byte(riskTradeDto.getOpenFlag()));
        riskTrade.setFailureTime(DateUtil.getEndDay(new Date())); //设置失效时间
        riskTrade.setCreator(riskTradeDto.getCreator());
        this.save(riskTrade);
    }

    @Override
    public RiskTradeDto get() {
        RiskTrade riskTrade = this.find();
        RiskTradeDto riskTradeDto = BeanUtil.copyProperties(riskTrade, RiskTradeDto.class);
        return riskTradeDto;
    }

    @Override
    public RiskTrade find() {
        Condition condition = new Condition(RiskTrade.class);
        Example.Criteria criteria = condition.createCriteria();
        Date now = new Date();
        criteria.andGreaterThan("failureTime", now);
        criteria.andLessThan("effectTime", now);
        criteria.andEqualTo("flag", new Byte(Constants.Flag.VALID));
        return findOneByCondition(condition);
    }

    @Override
    public boolean isRiskTrade() {
        RiskTrade riskTrade = find();
        return riskTrade != null && "0".equals(riskTrade.getOpenFlag().toString());
    }
}
