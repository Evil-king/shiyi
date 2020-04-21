package com.baibei.shiyi.trade.service.impl;

import com.baibei.shiyi.account.feign.bean.dto.CustomerNoDto;
import com.baibei.shiyi.account.feign.bean.dto.CustomerNoPageDto;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.enumeration.HoldResourceEnum;
import com.baibei.shiyi.common.tool.exception.SystemException;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.common.tool.utils.IdWorker;
import com.baibei.shiyi.common.tool.utils.NumberUtil;
import com.baibei.shiyi.trade.common.bo.ChangeHoldPositionBo;
import com.baibei.shiyi.trade.common.dto.MyHoldDto;
import com.baibei.shiyi.trade.common.vo.HoldDetailListVo;
import com.baibei.shiyi.trade.common.vo.MyHoldVo;
import com.baibei.shiyi.trade.common.vo.TotalMarketValueVo;
import com.baibei.shiyi.trade.dao.HoldDetailsMapper;
import com.baibei.shiyi.trade.feign.bean.dto.CustomerHoldPageListDto;
import com.baibei.shiyi.trade.feign.bean.vo.CustomerHoldPageListVo;
import com.baibei.shiyi.trade.model.HoldDetails;
import com.baibei.shiyi.trade.model.TradeConfig;
import com.baibei.shiyi.trade.service.IHoldDetailsService;
import com.baibei.shiyi.trade.service.IHoldPositionService;
import com.baibei.shiyi.trade.service.ITradeConfigService;
import com.baibei.shiyi.trade.service.ITradeDayService;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/05/28 19:42:41
 * @description: HoldDetails服务实现
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class HoldDetailsServiceImpl extends AbstractService<HoldDetails> implements IHoldDetailsService {
    @Autowired
    private HoldDetailsMapper traHoldDetailsMapper;
    @Autowired
    private ITradeConfigService tradeConfigService;
    @Autowired
    private IHoldPositionService holdPositionService;
    @Autowired
    private ITradeDayService tradeDayService;

    @Override
    public void save(ChangeHoldPositionBo bo) {
        TradeConfig tradeConfig = tradeConfigService.findFromCache();
        if (tradeConfig == null || tradeConfig.getTradeFrozenDay() == null) {
            throw new SystemException("交易配置或者冻结日期配置不存在");
        }
        HoldDetails details = new HoldDetails();
        details.setId(IdWorker.getId());
        details.setCustomerNo(bo.getCustomerNo());
        details.setCustomerName(bo.getCustomerName());
        details.setProductTradeNo(bo.getProductTradeNo());
        details.setCount(bo.getCount());
        details.setBuyPrice(bo.getPrice());
        if (bo.getTradeTime() == null) {
            if (0 == tradeConfig.getTradeFrozenDay().intValue()) {
                details.setTradeTime(new Date());
            } else {
                details.setTradeTime(tradeDayService.getAddNTradeDay(tradeConfig.getTradeFrozenDay()));
            }
        } else {
            details.setTradeTime(bo.getTradeTime());
        }
        details.setResource(bo.getResource());
        details.setUnlockFlag(Byte.valueOf("0"));
        details.setCreateTime(new Date());
        details.setHoldNo(bo.getResourceNo());
        save(details);
    }

    @Override
    public ApiResult<List<MyHoldVo>> myHoldList(MyHoldDto myHoldDto) {
        List<MyHoldVo> myHoldVo = traHoldDetailsMapper.myHoldList(myHoldDto.getCustomerNo());
        return ApiResult.success(myHoldVo);
    }

    @Override
    public MyPageInfo<CustomerHoldPageListVo> mypageList(CustomerHoldPageListDto customerHoldPageListDto) {
        PageHelper.startPage(customerHoldPageListDto.getCurrentPage(), customerHoldPageListDto.getPageSize());
        List<CustomerHoldPageListVo> customerHoldPageListVos = export(customerHoldPageListDto);
        MyPageInfo<CustomerHoldPageListVo> myPageInfo = new MyPageInfo<>(customerHoldPageListVos);
        return myPageInfo;
    }

    @Override
    public List<CustomerHoldPageListVo> export(CustomerHoldPageListDto customerHoldPageListDto) {
        List<CustomerHoldPageListVo> customerHoldPageListVos = traHoldDetailsMapper.mypageList(customerHoldPageListDto);
        customerHoldPageListVos.stream().forEach(x -> x.setResourceText(HoldResourceEnum.getDesc(x.getResource())));
        return customerHoldPageListVos;
    }

    @Override
    public ApiResult<TotalMarketValueVo> getTotalMarketValue(CustomerNoDto customerNoDto) {
//        //先查询tbl_tra_hold_position表
//        HoldPosition holdPosition = holdPositionService.selectByCustomer(customerNoDto.getCustomerNo());
//        //获取商品最新价
//        BigDecimal lastPrice = commonQuoteService.getLastPrice(holdPosition.getProductTradeNo());
//        TotalMarketValueVo totalMarketValueVo = traHoldDetailsMapper.getTotalMarketValue(customerNoDto.getCustomerNo(), lastPrice);
        return ApiResult.success();
    }

    @Override
    public MyPageInfo<HoldDetailListVo> getPageList(CustomerNoPageDto customerNoDto) {
        PageHelper.startPage(customerNoDto.getCurrentPage(), customerNoDto.getPageSize());
        List<HoldDetailListVo> holdDetailListVos = traHoldDetailsMapper.getPageList(customerNoDto);
        MyPageInfo<HoldDetailListVo> myPageInfo = new MyPageInfo<>(holdDetailListVos);
        return myPageInfo;
    }

    @Override
    public List<HoldDetails> findCanTradeList() {
        Condition condition = new Condition(HoldDetails.class);
        Example.Criteria criteria = buildValidCriteria(condition);
        criteria.andEqualTo("unlockFlag", 0);
        criteria.andLessThanOrEqualTo("tradeTime", new Date());
        return findByCondition(condition);
    }

    @Override
    public void calculationCost(String customerNo, String productTradeNo) {
        // 持仓成本价=（持仓数量*持仓价格）/总持仓数量
        BigDecimal cost = traHoldDetailsMapper.calculationCost(customerNo, productTradeNo);
        if (cost != null) {
            holdPositionService.updateCost(customerNo, productTradeNo, NumberUtil.roundDown(cost, 2));
        }
    }
}
