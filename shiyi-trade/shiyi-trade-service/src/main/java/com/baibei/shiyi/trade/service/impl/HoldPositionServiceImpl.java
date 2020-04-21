package com.baibei.shiyi.trade.service.impl;

import com.alibaba.fastjson.JSON;
import com.baibei.shiyi.account.feign.bean.dto.CustomerNoDto;
import com.baibei.shiyi.account.feign.bean.dto.CustomerNoPageDto;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.common.tool.utils.CollectionUtils;
import com.baibei.shiyi.quotation.feign.bean.vo.QuoteVo;
import com.baibei.shiyi.quotation.feign.service.ICommonQuoteService;
import com.baibei.shiyi.trade.common.dto.HoldPositionListDto;
import com.baibei.shiyi.trade.common.vo.HoldDetailListVo;
import com.baibei.shiyi.trade.common.vo.TotalMarketValueVo;
import com.baibei.shiyi.trade.dao.HoldPositionMapper;
import com.baibei.shiyi.trade.feign.bean.dto.HoldPositionDto;
import com.baibei.shiyi.trade.feign.bean.vo.HoldPositionVo;
import com.baibei.shiyi.trade.model.HoldPosition;
import com.baibei.shiyi.trade.model.Product;
import com.baibei.shiyi.trade.service.IHoldPositionService;
import com.baibei.shiyi.trade.service.IProductService;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.List;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/12/27 11:27:48
 * @description: HoldPosition服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class HoldPositionServiceImpl extends AbstractService<HoldPosition> implements IHoldPositionService {
    @Autowired
    private HoldPositionMapper holdPositionMapper;
    @Autowired
    private ICommonQuoteService commonQuoteService;
    @Autowired
    private IProductService productService;

    @Override
    public List<HoldPosition> selectByCustomer(String customerNo) {
        Condition condition = new Condition(HoldPosition.class);
        condition.orderBy("createTime").asc();
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("customerNo", customerNo);
        criteria.andEqualTo("flag", 1);
        List<HoldPosition> holdPositions = holdPositionMapper.selectByCondition(condition);
        if (!CollectionUtils.isEmpty(holdPositions)) {
            return holdPositions;
        }
        return null;
    }

    @Override
    public HoldPosition selectByCustomerAndProductTradeNo(String customerNo, String productTradeNo) {
        Condition condition = new Condition(HoldPosition.class);
        condition.orderBy("createTime").asc();
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("customerNo", customerNo);
        criteria.andEqualTo("productTradeNo", productTradeNo);
        criteria.andEqualTo("flag", 1);
        List<HoldPosition> holdPositions = holdPositionMapper.selectByCondition(condition);
        if (!CollectionUtils.isEmpty(holdPositions)) {
            return holdPositions.get(0);
        }
        return null;
    }


    @Override
    public ApiResult<TotalMarketValueVo> getTotalMarketValue(CustomerNoDto customerNoDto) {
        TotalMarketValueVo totalMarketValueVo = null;
        BigDecimal lastPrice = new BigDecimal(0);
        BigDecimal totalMarketValue = new BigDecimal(0);
        BigDecimal totalProfitAndLoss = new BigDecimal(0);
        //先查询tbl_tra_hold_position表
        List<HoldPosition> holdPosition = this.selectByCustomer(customerNoDto.getCustomerNo());
        if (CollectionUtils.isEmpty(holdPosition)) {
            return ApiResult.success(new TotalMarketValueVo());
        }
        for (int i = 0; i < holdPosition.size(); i++) {
            //获取商品最新价
            QuoteVo quoteInfo = commonQuoteService.getQuoteInfo(holdPosition.get(i).getProductTradeNo());
            log.info("获取行情信息,quoteInfo={}", JSON.toJSONString(quoteInfo));
            if (quoteInfo.getLastPrice().indexOf("--.--") == -1) {
                lastPrice = new BigDecimal(quoteInfo.getLastPrice());
                totalMarketValueVo = holdPositionMapper.getTotalMarketValue(customerNoDto.getCustomerNo(), lastPrice,
                        holdPosition.get(i).getProductTradeNo());
                totalMarketValue = totalMarketValue.add(totalMarketValueVo.getTotalMarketValue());
                totalProfitAndLoss = totalProfitAndLoss.add(totalMarketValueVo.getTotalProfitAndLoss());
            }
            if (quoteInfo.getLastPrice().indexOf("--.--") > -1 && !ObjectUtils.isEmpty(quoteInfo.getYestPrice())) {
                lastPrice = quoteInfo.getYestPrice();
                totalMarketValueVo = holdPositionMapper.getTotalMarketValue(customerNoDto.getCustomerNo(), lastPrice,
                        holdPosition.get(i).getProductTradeNo());
                totalMarketValue = totalMarketValue.add(totalMarketValueVo.getTotalMarketValue());
                totalProfitAndLoss = totalProfitAndLoss.add(totalMarketValueVo.getTotalProfitAndLoss());
            } else {
                new TotalMarketValueVo();
            }
        }
        log.info("totalMarketValue={},totalProfitAndLoss={}", totalMarketValue, totalProfitAndLoss);
        totalMarketValueVo.setTotalProfitAndLoss(totalProfitAndLoss);
        totalMarketValueVo.setTotalMarketValue(totalMarketValue);
        return ApiResult.success(totalMarketValueVo == null ? new TotalMarketValueVo() : totalMarketValueVo);
    }

    @Override
    public MyPageInfo<HoldDetailListVo> getPageList(CustomerNoPageDto customerNoDto) {
        PageHelper.startPage(customerNoDto.getCurrentPage(), customerNoDto.getPageSize());
        List<HoldDetailListVo> holdDetailListVos = Lists.newArrayList();
        BigDecimal lastPrice = new BigDecimal(0);
        //先查询tbl_tra_hold_position表
        List<HoldPosition> holdPosition = this.selectByCustomer(customerNoDto.getCustomerNo());
        if (CollectionUtils.isEmpty(holdPosition)) {
            return new MyPageInfo<HoldDetailListVo>(holdDetailListVos);
        }
        for (int i = 0; i < holdPosition.size(); i++) {
            //获取商品最新价
            QuoteVo quoteInfo = commonQuoteService.getQuoteInfo(holdPosition.get(i).getProductTradeNo());
            log.info("获取行情信息,quoteInfo={}", JSON.toJSONString(quoteInfo));
            if (quoteInfo.getLastPrice().indexOf("--.--") == -1) {
                lastPrice = new BigDecimal(quoteInfo.getLastPrice());
                HoldDetailListVo pageList = holdPositionMapper
                        .getPageList(customerNoDto.getCustomerNo(), lastPrice, holdPosition.get(i).getProductTradeNo());
                holdDetailListVos.add(pageList);
            }
            if (quoteInfo.getLastPrice().indexOf("--.--") > -1 && !ObjectUtils.isEmpty(quoteInfo.getYestPrice())) {
                lastPrice = quoteInfo.getYestPrice();
                HoldDetailListVo pageList = holdPositionMapper
                        .getPageList(customerNoDto.getCustomerNo(), lastPrice, holdPosition.get(i).getProductTradeNo());
                holdDetailListVos.add(pageList);
            }
            if (quoteInfo.getLastPrice().indexOf("--.--") == -1 && ObjectUtils.isEmpty(quoteInfo.getYestPrice())) {
                Product effective = productService.findEffective(holdPosition.get(i).getProductTradeNo());
                if (effective != null) {
                    HoldDetailListVo holdDetailListVo = new HoldDetailListVo()
                            .setProductImg(effective.getProductIcon())
                            .setProductName(effective.getProductName())
                            .setProductTradeNo(effective.getProductTradeNo())
                            .setCustomerNo(holdPosition.get(i).getCustomerNo());
                    holdDetailListVos.add(holdDetailListVo);
                }
            }
        }
        MyPageInfo<HoldDetailListVo> myPageInfo = new MyPageInfo<>(holdDetailListVos);
        return myPageInfo;
    }

    @Override
    public HoldPosition find(String customerNo, String productTradeNo) {
        Assert.hasText(customerNo, "customerNo cannot be null");
        Assert.hasText(productTradeNo, "productTradeNo cannot be null");
        Condition condition = new Condition(HoldPosition.class);
        Example.Criteria criteria = buildValidCriteria(condition);
        criteria.andEqualTo("customerNo", customerNo);
        criteria.andEqualTo("productTradeNo", productTradeNo);
        return findOneByCondition(condition);
    }

    @Override
    public MyPageInfo<HoldPositionVo> pageList(HoldPositionDto holdPositionDto) {
        if (holdPositionDto.getPageSize() != null && holdPositionDto.getCurrentPage() != null) {
            PageHelper.startPage(holdPositionDto.getCurrentPage(),holdPositionDto.getPageSize());
        }
        List<HoldPositionVo> holdPositionVoList = holdPositionMapper.findPageList(holdPositionDto);
        holdPositionVoList.stream().forEach(x -> {
            // TODO: 2019/12/28 用持仓单表的数据计算，不要查明细
            BigDecimal lastPrice = commonQuoteService.getLastPrice(x.getProductTradeNo());
            if (lastPrice != null) {
                x.setLastPrice(lastPrice);
                // 持仓市值 最新价*剩余数量
                BigDecimal marketValue = lastPrice.multiply(new BigDecimal(x.getRemaindCount())).setScale(2, BigDecimal.ROUND_DOWN);
                x.setHoldMarketValue(marketValue);
                //盈亏资金 持有总数*(最新价格-成本价)
                if (x.getCostPrice() != null) {
                    BigDecimal profitAndLossPrice = lastPrice.subtract(x.getCostPrice()).multiply(new BigDecimal(x.getRemaindCount()));
                    if (profitAndLossPrice != null) {
                        x.setProfitAndLossPrice(profitAndLossPrice);
                        // 盈亏比例 (盈亏资金/(持用总数*成本价)*100%)
                        BigDecimal countCostPost = x.getCostPrice().multiply(new BigDecimal(x.getRemaindCount()));
                        if (countCostPost.compareTo(BigDecimal.ZERO) > 0) {
                            BigDecimal profitLossRatio = profitAndLossPrice.divide(countCostPost, 2, BigDecimal.ROUND_DOWN);
                            x.setProfitLossRatio(profitLossRatio);
                        }
                    }
                }
            }
        });
        return new MyPageInfo<>(holdPositionVoList);
    }


    @Override
    public void updateCost(String customerNo, String productTradeNo, BigDecimal cost) {
        Assert.notNull(customerNo, "customerNo cannot be null");
        Assert.notNull(productTradeNo, "productTradeNo cannot be null");
        Assert.notNull(cost, "cost cannot be null");
        HoldPosition newPosition = new HoldPosition();
        newPosition.setCostPrice(cost);
        Condition condition = new Condition(HoldPosition.class);
        condition.createCriteria().andEqualTo("customerNo", customerNo)
                .andEqualTo("productTradeNo", productTradeNo);
        updateByConditionSelective(newPosition, condition);
    }

    @Override
    public Integer sumCanSellCount(String productTradeNo) {
        return holdPositionMapper.sumCanSellCount(productTradeNo);
    }

    @Override
    public MyPageInfo<HoldDetailListVo> getPcPageList(HoldPositionListDto holdPositionListDto) {
        PageHelper.startPage(holdPositionListDto.getCurrentPage(), holdPositionListDto.getPageSize());
        List<HoldDetailListVo> holdDetailListVos = Lists.newArrayList();
        BigDecimal lastPrice = new BigDecimal(0);
        //先查询tbl_tra_hold_position表
        HoldPosition holdPosition = this.selectByCustomerAndProductTradeNo(holdPositionListDto.getCustomerNo(),
                holdPositionListDto.getProductTradeNo());
        if (ObjectUtils.isEmpty(holdPosition)) {
            return new MyPageInfo<HoldDetailListVo>(holdDetailListVos);
        }
        //获取商品最新价
        QuoteVo quoteInfo = commonQuoteService.getQuoteInfo(holdPosition.getProductTradeNo());
        log.info("获取行情信息,quoteInfo={}", JSON.toJSONString(quoteInfo));
        if (quoteInfo.getLastPrice().indexOf("--.--") == -1) {
            lastPrice = new BigDecimal(quoteInfo.getLastPrice());
            HoldDetailListVo pageList = holdPositionMapper
                    .getPageList(holdPositionListDto.getCustomerNo(), lastPrice, holdPosition.getProductTradeNo());
            holdDetailListVos.add(pageList);
        }
        if (quoteInfo.getLastPrice().indexOf("--.--") > -1 && !ObjectUtils.isEmpty(quoteInfo.getYestPrice())) {
            lastPrice = quoteInfo.getYestPrice();
            HoldDetailListVo pageList = holdPositionMapper
                    .getPageList(holdPositionListDto.getCustomerNo(), lastPrice, holdPosition.getProductTradeNo());
            holdDetailListVos.add(pageList);
        } else {
            Product effective = productService.findEffective(holdPosition.getProductTradeNo());
            if (effective != null) {
                HoldDetailListVo holdDetailListVo = new HoldDetailListVo()
                        .setProductImg(effective.getProductIcon())
                        .setProductName(effective.getProductName())
                        .setProductTradeNo(effective.getProductTradeNo())
                        .setCustomerNo(holdPosition.getCustomerNo());
                holdDetailListVos.add(holdDetailListVo);
            }
        }
        MyPageInfo<HoldDetailListVo> myPageInfo = new MyPageInfo<>(holdDetailListVos);
        return myPageInfo;
    }
}
