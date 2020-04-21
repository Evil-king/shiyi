package com.baibei.shiyi.trade.utils;

import com.alibaba.fastjson.JSON;
import com.baibei.component.redis.util.RedisUtil;
import com.baibei.shiyi.common.tool.constants.RedisConstant;
import com.baibei.shiyi.common.tool.exception.SystemException;
import com.baibei.shiyi.common.tool.utils.MapUtil;
import com.baibei.shiyi.common.tool.utils.NumberUtil;
import com.baibei.shiyi.quotation.feign.bean.dto.QuoteDto;
import com.baibei.shiyi.trade.model.FeeExemptionConfig;
import com.baibei.shiyi.trade.model.TradeConfig;
import com.baibei.shiyi.trade.service.IFeeExemptionConfigService;
import com.baibei.shiyi.trade.service.ITradeConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/12/27 17:45
 * @description:
 */
@Slf4j
@Component
public class TradeUtil {
    private static final BigDecimal oneHundred = new BigDecimal("100");
    @Autowired
    private IFeeExemptionConfigService feeExemptionConfigService;
    @Autowired
    private ITradeConfigService tradeConfigService;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 计算买入手续费
     *
     * @param customerNo
     * @param price
     * @param count
     * @return
     */
    public BigDecimal getBuyFee(String customerNo, BigDecimal price, Integer count) {
        return getFee(customerNo, price, count, "buyFee");
    }


    /**
     * 计算卖出手续费
     *
     * @param customerNo
     * @param price
     * @param count
     * @return
     */
    public BigDecimal getSellFee(String customerNo, BigDecimal price, Integer count) {
        return getFee(customerNo, price, count, "sellFee");
    }

    /**
     * 手续费计算
     *
     * @param customerNo
     * @param price
     * @param count
     * @param feeType
     * @return
     */
    private BigDecimal getFee(String customerNo, BigDecimal price, Integer count, String feeType) {
        BigDecimal cost = price.multiply(new BigDecimal(count));
        // 是否在手续费豁免名单中
        FeeExemptionConfig feeExemptionConfig = feeExemptionConfigService.getByCustomerNo(customerNo);
        if (feeExemptionConfig != null) {
            return BigDecimal.ZERO;
        }
        // 计算手续费
        BigDecimal feeRate;
        Map<String, Object> tradeConfigCache = redisUtil.hgetAll(RedisConstant.TRADE_CONFIG);
        if (tradeConfigCache != null && tradeConfigCache.get(feeType) != null) {
            feeRate = new BigDecimal(tradeConfigCache.get(feeType).toString());
        } else {
            TradeConfig tradeConfig = tradeConfigService.find();
            if (tradeConfig == null) {
                throw new SystemException("交易配置信息不存在");
            }
            redisUtil.hsetAll(RedisConstant.TRADE_CONFIG, MapUtil.objectToMap(tradeConfig));
            feeRate = "buyFee".equals(feeType) ? tradeConfig.getBuyFee() : tradeConfig.getSellFee();
        }
        BigDecimal fee = NumberUtil.roundDown(cost.multiply(feeRate.divide(oneHundred)), 2);
        return fee;
    }

    /**
     * 获取客户单个持仓的买入手续费
     *
     * @param customerNo
     * @param price
     * @return
     */
    public BigDecimal getSingleBuyFee(String customerNo, BigDecimal price) {
        // 是否在手续费豁免名单中
        FeeExemptionConfig feeExemptionConfig = feeExemptionConfigService.getByCustomerNo(customerNo);
        if (feeExemptionConfig != null) {
            return BigDecimal.ZERO;
        }
        // 计算手续费
        BigDecimal feeRate;
        Map<String, Object> tradeConfigCache = redisUtil.hgetAll(RedisConstant.TRADE_CONFIG);
        if (tradeConfigCache != null && tradeConfigCache.get("buyFee") != null) {
            feeRate = new BigDecimal(tradeConfigCache.get("buyFee").toString());
        } else {
            TradeConfig tradeConfig = tradeConfigService.find();
            if (tradeConfig == null) {
                throw new SystemException("交易配置信息不存在");
            }
            redisUtil.hsetAll(RedisConstant.TRADE_CONFIG, MapUtil.objectToMap(tradeConfig));
            feeRate = tradeConfig.getBuyFee();
        }
        BigDecimal fee = NumberUtil.roundDown(price.multiply(feeRate.divide(oneHundred)), 2);
        return fee;
    }

    /**
     * 推送委托单、成交单、撤单数据
     *
     * @param operateType
     * @param productTradeNo
     * @param price
     * @param count
     */
    public void pushTradeOrder(String operateType, String productTradeNo, BigDecimal price,
                               Integer count, String orderNo, Date occurTime) {
        QuoteDto quoteDto = new QuoteDto();
        quoteDto.setOperateType(operateType);
        quoteDto.setProductTradeNo(productTradeNo);
        quoteDto.setPrice(price);
        quoteDto.setDealCount(new BigDecimal(count));
        quoteDto.setDealtime(occurTime.getTime());
        quoteDto.setOrderNo(orderNo);
        redisUtil.leftPush(MessageFormat.format(RedisConstant.QUOTESERVICE_QUOTE_LIST, productTradeNo), JSON.toJSONString(quoteDto));
    }
}