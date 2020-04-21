package com.baibei.shiyi.trade.service.impl;

import com.baibei.component.redis.util.RedisUtil;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.constants.RedisConstant;
import com.baibei.shiyi.common.tool.enumeration.DealOrderTypeEnum;
import com.baibei.shiyi.common.tool.enumeration.EntrustOrderResultEnum;
import com.baibei.shiyi.common.tool.exception.SystemException;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.common.tool.page.PageUtil;
import com.baibei.shiyi.common.tool.utils.CollectionUtils;
import com.baibei.shiyi.common.tool.utils.IdWorker;
import com.baibei.shiyi.common.tool.utils.MapUtil;
import com.baibei.shiyi.quotation.feign.service.ICommonQuoteService;
import com.baibei.shiyi.trade.common.bo.ListBo;
import com.baibei.shiyi.trade.common.dto.EntrustOrderListDto;
import com.baibei.shiyi.trade.common.dto.MyEntrustDetailsDto;
import com.baibei.shiyi.trade.common.dto.PcEntrustListAndCustomerDto;
import com.baibei.shiyi.trade.common.dto.PcEntrustListDto;
import com.baibei.shiyi.trade.common.vo.EntrustOrderListVo;
import com.baibei.shiyi.trade.common.vo.MyEntrustDetailsVo;
import com.baibei.shiyi.trade.common.vo.MyEntrustOrderVo;
import com.baibei.shiyi.trade.dao.EntrustOrderMapper;
import com.baibei.shiyi.trade.feign.bean.dto.CustomerEntrustOrderListDto;
import com.baibei.shiyi.trade.feign.bean.vo.CustomerEntrustOrderListVo;
import com.baibei.shiyi.trade.model.EntrustOrder;
import com.baibei.shiyi.trade.model.TradeConfig;
import com.baibei.shiyi.trade.service.IEntrustOrderService;
import com.baibei.shiyi.trade.service.ITradeConfigService;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/05/24 16:03:43
 * @description: EntrustOrder服务实现
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class EntrustOrderServiceImpl extends AbstractService<EntrustOrder> implements IEntrustOrderService {
    @Autowired
    private EntrustOrderMapper tblEntrustOrderMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ITradeConfigService tradeConfigService;
    @Autowired
    private ICommonQuoteService quoteService;


    @Override
    public void save(ListBo listBo) {
        EntrustOrder entrustOrder = new EntrustOrder();
        entrustOrder.setId(IdWorker.getId());
        entrustOrder.setCustomerNo(listBo.getCustomerNo());
        entrustOrder.setCustomerName(listBo.getCustomerName());
        entrustOrder.setProductTradeNo(listBo.getProductTradeNo());
        entrustOrder.setEntrustNo(listBo.getEntrustNo());
        entrustOrder.setEntrustTime(listBo.getEntrustTime());
        entrustOrder.setDirection(listBo.getDirection());
        entrustOrder.setPrice(listBo.getPrice());
        entrustOrder.setEntrustCount(listBo.getCount());
        entrustOrder.setWaitCount(listBo.getCount());
        entrustOrder.setDealCount(0);
        entrustOrder.setRevokeCount(0);
        entrustOrder.setResult(EntrustOrderResultEnum.WAIT_DEAL.getCode());
        entrustOrder.setAnonymousFlag(listBo.getAnonymousFlag());
        entrustOrder.setStatus(listBo.getStatus());
        entrustOrder.setCreateTime(new Date());
        entrustOrder.setChangeAmount(listBo.getChangeAmount());
        entrustOrder.setBuyFee(listBo.getBuyFee());
        save(entrustOrder);
    }

    @Override
    public void updateEntrustStatus(String entrustNo, String status) {
        Condition condition = new Condition(EntrustOrder.class);
        buildValidCriteria(condition).andEqualTo("entrustNo", entrustNo);
        EntrustOrder entrustOrder = new EntrustOrder();
        entrustOrder.setStatus(status);
        updateByConditionSelective(entrustOrder, condition);
    }

    @Override
    public void updateEntrustByDelist(EntrustOrder entrustOrder, Integer count) {
        Condition condition = new Condition(EntrustOrder.class);
        buildValidCriteria(condition).andEqualTo("entrustNo", entrustOrder.getEntrustNo())
                .andEqualTo("dealCount", entrustOrder.getDealCount())
                .andEqualTo("waitCount", entrustOrder.getWaitCount());
        EntrustOrder newEntrustOrder = new EntrustOrder();
        newEntrustOrder.setModifyTime(new Date());
        newEntrustOrder.setDealCount(entrustOrder.getDealCount() + count);
        newEntrustOrder.setWaitCount(entrustOrder.getWaitCount() - count);
        if (newEntrustOrder.getWaitCount() <= 0) {
            newEntrustOrder.setResult(EntrustOrderResultEnum.ALL_DEAL.getCode());
        }
        boolean flag = updateByConditionSelective(newEntrustOrder, condition);
        if (!flag) {
            throw new SystemException("更新委托单失败");
        }
    }

    @Override
    public MyPageInfo<EntrustOrderListVo> entrustOrderList(EntrustOrderListDto dto) {
        PageHelper.startPage(dto.getCurrentPage(), dto.getPageSize());
        Map<String, Object> param = new HashMap<>();
        param.put("direction", dto.getDirection());
        param.put("productTradeNo", dto.getProductTradeNo());
        param.put("result", EntrustOrderResultEnum.WAIT_DEAL.getCode());
        // 是否最优价格成交
        Map<String, Object> tradeConfigCache = redisUtil.hgetAll(RedisConstant.TRADE_CONFIG);
        String bestPriceDeal;
        if (tradeConfigCache != null && tradeConfigCache.get("bestPriceDeal") != null) {
            bestPriceDeal = tradeConfigCache.get("bestPriceDeal").toString();
        } else {
            TradeConfig tradeConfig = tradeConfigService.find();
            if (tradeConfig == null) {
                throw new SystemException("交易配置信息不存在");
            }
            redisUtil.hsetAll(RedisConstant.TRADE_CONFIG, MapUtil.objectToMap(tradeConfig));
            bestPriceDeal = tradeConfig.getBestPriceDeal().toString();
        }
        if ("1".equals(bestPriceDeal)) {
            String quotationOperateType = Constants.TradeDirection.BUY.equals(dto.getDirection())
                    ? Constants.QuotationPosition.HANG_BUY : Constants.QuotationPosition.HANG_SELL;
            BigDecimal firstPosition = quoteService.getTheFistPositionPrice(dto.getProductTradeNo(), quotationOperateType);
            // 获取不到最优价，返回空列表
            if (firstPosition == null) {
                log.info("获取最优价失败");
                return PageUtil.getEmptyPageInfo();
            }
            param.put("price", firstPosition);
        }
        List<EntrustOrderListVo> list = tblEntrustOrderMapper.entrustOrderList(param);
        if (CollectionUtils.isEmpty(list)) {
            return PageUtil.getEmptyPageInfo();
        }
        list.forEach(vo -> {
            vo.setCustomerName(vo.getAnonymousFlag().intValue() == 1 ? "匿名" : vo.getCustomerName());
            vo.setCanRevokeCount(vo.getEntrustCount() - vo.getDealCount());
        });
        return new MyPageInfo<>(list);
    }

    @Override
    public ApiResult<List<MyEntrustOrderVo>> myEntrustList(String customerNo) {
        List<MyEntrustOrderVo> EntrustOrderList = tblEntrustOrderMapper.myEntrustList(customerNo);
        EntrustOrderList.stream().forEach(result -> {
            if (EntrustOrderResultEnum.WAIT_DEAL.getCode().equals(result.getResult())) {
                result.setResultDesc(EntrustOrderResultEnum.WAIT_DEAL.getMsg());
            }
            if (DealOrderTypeEnum.BUY.getCode().equals(result.getDirection())) {
                result.setDirectionDesc(DealOrderTypeEnum.BUY.getMsg());
            }
            if (DealOrderTypeEnum.SELL.getCode().equals(result.getDirection())) {
                result.setDirectionDesc(DealOrderTypeEnum.SELL.getMsg());
            }
        });
        return ApiResult.success(EntrustOrderList);
    }

    @Override
    public ApiResult<MyEntrustDetailsVo> myEntrustDetails(MyEntrustDetailsDto myEntrustDetailsDto) {
        MyEntrustDetailsVo myEntrustDetailsVo = tblEntrustOrderMapper.myEntrustDetails(myEntrustDetailsDto.getEntrustId()
                , myEntrustDetailsDto.getCustomerNo());
        myEntrustDetailsVo.setCustomerNo(changeMobile(myEntrustDetailsVo.getCustomerNo()));
        return ApiResult.success(myEntrustDetailsVo);
    }

    @Override
    public EntrustOrder findByOrderNo(String entrustOrderNo) {
        return findBy("entrustNo", entrustOrderNo);
    }


    @Override
    public MyPageInfo<CustomerEntrustOrderListVo> mypageList(CustomerEntrustOrderListDto customerEntrustOrderListDto) {
        PageHelper.startPage(customerEntrustOrderListDto.getCurrentPage(), customerEntrustOrderListDto.getPageSize());
        List<CustomerEntrustOrderListVo> customerEntrustOrderListVos = export(customerEntrustOrderListDto);
        MyPageInfo<CustomerEntrustOrderListVo> myPageInfo = new MyPageInfo<>(customerEntrustOrderListVos);
        return myPageInfo;
    }

    @Override
    public List<CustomerEntrustOrderListVo> export(CustomerEntrustOrderListDto customerEntrustOrderListDto) {
        List<CustomerEntrustOrderListVo> customerEntrustOrderListVos = tblEntrustOrderMapper.mypageList(customerEntrustOrderListDto);
        for (int i = 0; i < customerEntrustOrderListVos.size(); i++) {
            CustomerEntrustOrderListVo customerEntrustOrderListVo = customerEntrustOrderListVos.get(i);
            customerEntrustOrderListVo.setResult(EntrustOrderResultEnum.getMsg(customerEntrustOrderListVo.getResult()));
            if ("buy".equalsIgnoreCase(customerEntrustOrderListVo.getDirection())) {
                customerEntrustOrderListVo.setDirection("委托买入");
            } else if ("sell".equalsIgnoreCase(customerEntrustOrderListVo.getDirection())) {
                customerEntrustOrderListVo.setDirection("委托卖出");
            }
        }
        return customerEntrustOrderListVos;
    }

    @Override
    public List<EntrustOrder> listByResult(String result) {
        Assert.hasText(result, "result不能为空");
        Condition condition = new Condition(EntrustOrder.class);
        Example.Criteria criteria = buildValidCriteria(condition);
        criteria.andEqualTo("result", result);
        return findByCondition(condition);
    }

    @Override
    public List<EntrustOrder> listByParam(String customerNo, String direction, String productTradeNo, String result) {
        Condition condition = new Condition(EntrustOrder.class);
        Example.Criteria criteria = buildValidCriteria(condition);
        if (!StringUtils.isEmpty(customerNo)) {
            criteria.andEqualTo("customerNo", customerNo);
        }
        if (!StringUtils.isEmpty(direction)) {
            criteria.andEqualTo("direction", direction);
        }
        if (!StringUtils.isEmpty(productTradeNo)) {
            criteria.andEqualTo("productTradeNo", productTradeNo);
        }
        if (!StringUtils.isEmpty(result)) {
            criteria.andEqualTo("result", result);
        }
        return findByCondition(condition);
    }

    @Override
    public void revoke(EntrustOrder entrustOrder, String result) {
        EntrustOrder newOrder = new EntrustOrder();
        newOrder.setModifyTime(new Date());
        newOrder.setResult(result);
        newOrder.setRevokeCount(entrustOrder.getWaitCount());
        Condition condition = new Condition(EntrustOrder.class);
        condition.createCriteria()
                .andEqualTo("id", entrustOrder.getId())
                .andEqualTo("revokeCount", entrustOrder.getRevokeCount());
        boolean flag = updateByConditionSelective(newOrder, condition);
        if (!flag) {
            throw new SystemException("委托单【%s】撤单更新失败");
        }
    }

    @Override
    public ApiResult<List<MyEntrustOrderVo>> myPcEntrustList(PcEntrustListDto pcEntrustListDto) {
        List<MyEntrustOrderVo> EntrustOrderList = tblEntrustOrderMapper.myPcEntrustList(pcEntrustListDto.getCustomerNo(),
                pcEntrustListDto.getProductTradeNo(),pcEntrustListDto.getDirection());
        EntrustOrderList.stream().forEach(result -> {
            if (EntrustOrderResultEnum.WAIT_DEAL.getCode().equals(result.getResult())) {
                result.setResultDesc(EntrustOrderResultEnum.WAIT_DEAL.getMsg());
            }
            if (EntrustOrderResultEnum.ALL_DEAL.getCode().equals(result.getResult())) {
                result.setResultDesc(EntrustOrderResultEnum.ALL_DEAL.getMsg());
            }
            if (EntrustOrderResultEnum.SYSTEM_REVOKE.getCode().equals(result.getResult())) {
                result.setResultDesc(EntrustOrderResultEnum.SYSTEM_REVOKE.getMsg());
            }
            if (EntrustOrderResultEnum.CUSTOMER_REVOKE.getCode().equals(result.getResult())) {
                result.setResultDesc(EntrustOrderResultEnum.CUSTOMER_REVOKE.getMsg());
            }
            if (DealOrderTypeEnum.BUY.getCode().equals(result.getDirection())) {
                result.setDirectionDesc(DealOrderTypeEnum.BUY.getMsg());
            }
            if (DealOrderTypeEnum.SELL.getCode().equals(result.getDirection())) {
                result.setDirectionDesc(DealOrderTypeEnum.SELL.getMsg());
            }
        });
        return ApiResult.success(EntrustOrderList);
    }

    @Override
    public MyPageInfo<MyEntrustOrderVo> myPcEntrustListAndCustomerNo(PcEntrustListAndCustomerDto pcEntrustListAndCustomerDto) {
        PageHelper.startPage(pcEntrustListAndCustomerDto.getCurrentPage(), pcEntrustListAndCustomerDto.getPageSize());
        List<MyEntrustOrderVo> EntrustOrderList = tblEntrustOrderMapper.myPcEntrustListAndCustomerNo(pcEntrustListAndCustomerDto);
        EntrustOrderList.stream().forEach(result -> {
            if (EntrustOrderResultEnum.WAIT_DEAL.getCode().equals(result.getResult())) {
                result.setResultDesc(EntrustOrderResultEnum.WAIT_DEAL.getMsg());
            }
            if (EntrustOrderResultEnum.ALL_DEAL.getCode().equals(result.getResult())) {
                result.setResultDesc(EntrustOrderResultEnum.ALL_DEAL.getMsg());
            }
            if (EntrustOrderResultEnum.SYSTEM_REVOKE.getCode().equals(result.getResult())) {
                result.setResultDesc(EntrustOrderResultEnum.SYSTEM_REVOKE.getMsg());
            }
            if (EntrustOrderResultEnum.CUSTOMER_REVOKE.getCode().equals(result.getResult())) {
                result.setResultDesc(EntrustOrderResultEnum.CUSTOMER_REVOKE.getMsg());
            }
            if (DealOrderTypeEnum.BUY.getCode().equals(result.getDirection())) {
                result.setDirectionDesc(DealOrderTypeEnum.BUY.getMsg());
            }
            if (DealOrderTypeEnum.SELL.getCode().equals(result.getDirection())) {
                result.setDirectionDesc(DealOrderTypeEnum.SELL.getMsg());
            }
        });
        MyPageInfo<MyEntrustOrderVo> myPageInfo = new MyPageInfo<>(EntrustOrderList);
        return myPageInfo;
    }

    public static String changeMobile(String mobile) {
        if (StringUtils.isEmpty(mobile)) {
            return null;
        }
        return mobile.substring(0, 3) + "****" + mobile.substring(7, mobile.length());
    }
}
