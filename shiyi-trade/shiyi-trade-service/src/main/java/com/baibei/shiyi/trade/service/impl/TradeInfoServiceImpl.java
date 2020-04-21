package com.baibei.shiyi.trade.service.impl;

import com.alibaba.fastjson.JSON;
import com.baibei.shiyi.account.feign.bean.dto.CustomerNoDto;
import com.baibei.shiyi.account.feign.bean.vo.AccountVo;
import com.baibei.shiyi.account.feign.client.AccountFeign;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.exception.SystemException;
import com.baibei.shiyi.common.tool.utils.BeanUtil;
import com.baibei.shiyi.quotation.feign.bean.vo.PricePositionVo;
import com.baibei.shiyi.quotation.feign.bean.vo.QuoteVo;
import com.baibei.shiyi.quotation.feign.service.ICommonQuoteService;
import com.baibei.shiyi.trade.common.dto.TradeInfoDto;
import com.baibei.shiyi.trade.common.vo.PCTradeInfoVo;
import com.baibei.shiyi.trade.common.vo.TradeInfoVo;
import com.baibei.shiyi.trade.model.HoldPosition;
import com.baibei.shiyi.trade.model.Product;
import com.baibei.shiyi.trade.service.IHoldPositionService;
import com.baibei.shiyi.trade.service.IProductService;
import com.baibei.shiyi.trade.service.ITradeInfoService;
import com.baibei.shiyi.trade.utils.TradeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

/**
 * @author: 会跳舞的机器人
 * @date: 2020/1/7 14:46
 * @description:
 */
@Service
@Slf4j
public class TradeInfoServiceImpl implements ITradeInfoService {
    private static final BigDecimal oneHundred = new BigDecimal("100");
    private static final BigDecimal one = new BigDecimal("1");
    @Autowired
    private IProductService productService;
    @Autowired
    private IHoldPositionService holdPositionService;
    @Autowired
    private AccountFeign accountFeign;
    @Autowired
    private ICommonQuoteService commonQuoteService;
    @Autowired
    private TradeUtil tradeUtil;

    @Override
    public PCTradeInfoVo pcTradeInfoVo(TradeInfoDto dto) {
        TradeInfoVo tradeInfoVo = tradeInfo(dto);
        PCTradeInfoVo pcTradeInfoVo = BeanUtil.copyProperties(tradeInfoVo, PCTradeInfoVo.class);
        CustomerNoDto customerNoDto = new CustomerNoDto();
        customerNoDto.setCustomerNo(dto.getCustomerNo());
        ApiResult<AccountVo> accountResult = accountFeign.findAccount(customerNoDto);
        if (accountResult.hasFail()) {
            throw new SystemException("获取账户余额失败");
        }
        AccountVo accountVo = accountResult.getData();
        pcTradeInfoVo.setBalance(accountVo.getBalance());
        pcTradeInfoVo.setWithdrawableCash(accountVo.getWithdrawableCash());
        // 计算存货价值
        HoldPosition holdPosition = holdPositionService.find(dto.getCustomerNo(), dto.getProductTradeNo());
        if (holdPosition == null) {
            pcTradeInfoVo.setWorth(BigDecimal.ZERO);
        } else {
            pcTradeInfoVo.setWorth(tradeInfoVo.getLatestPrice().multiply(new BigDecimal(holdPosition.getRemaindCount())));
        }
        // 买卖档位
        PricePositionVo pricePositionVo = commonQuoteService.getPricePosition(dto.getProductTradeNo(), 3);
        if (pricePositionVo != null) {
            pcTradeInfoVo.setBuyPricePositionList(pricePositionVo.getBuyPricePositionList());
            pcTradeInfoVo.setSellPricePositionList(pricePositionVo.getSellPricePositionList());
        }
        return pcTradeInfoVo;
    }

    @Override
    public TradeInfoVo tradeInfo(TradeInfoDto dto) {
        TradeInfoVo vo = tradeInfoCommon(dto);
        if (Constants.TradeDirection.BUY.equals(dto.getDirection())) {
            CustomerNoDto customerNoDto = new CustomerNoDto();
            customerNoDto.setCustomerNo(dto.getCustomerNo());
            ApiResult<AccountVo> accountResult = accountFeign.findAccount(customerNoDto);
            if (accountResult.hasFail()) {
                throw new SystemException("获取账户余额失败");
            }
            BigDecimal balance = accountResult.getData().getBalance();
            BigDecimal fee = tradeUtil.getBuyFee(dto.getCustomerNo(), vo.getLatestPrice(), 1);
            vo.setCount(balance.divide(vo.getLatestPrice().add(fee), 2).intValue());
        } else {
            HoldPosition holdPosition = holdPositionService.find(dto.getCustomerNo(), dto.getProductTradeNo());
            vo.setCount(holdPosition == null ? 0 : holdPosition.getCanSellCount());
        }
        return vo;
    }

    /**
     * 获取数据信息
     *
     * @param dto
     * @return
     */
    private TradeInfoVo tradeInfoCommon(TradeInfoDto dto) {
        Product product = productService.findEffective(dto.getProductTradeNo());
        Assert.notNull(product, "获取商品信息为空");
        QuoteVo quoteVo = commonQuoteService.getQuoteInfo(dto.getProductTradeNo());
        Assert.notNull(quoteVo, "获取行情数据为空");
        log.info("获取最新的行情数据为【{}】", JSON.toJSONString(quoteVo));
        TradeInfoVo vo = new TradeInfoVo();
        vo.setProductTradeNo(product.getProductTradeNo());
        vo.setProductName(product.getProductName());
        BigDecimal latestPrice;
        // 没有最新价，就拿昨收价作为最新价
        if (StringUtils.isEmpty(quoteVo.getLastPrice()) || "--.--".equals(quoteVo.getLastPrice())) {
            latestPrice = quoteVo.getYestPrice();
        } else {
            latestPrice = new BigDecimal(quoteVo.getLastPrice());
        }
        BigDecimal yestPrice = quoteVo.getYestPrice();
        vo.setLatestPrice(latestPrice);
        // 涨幅：（最新价格-昨收价）/昨收价
        BigDecimal increaseRate = (latestPrice.subtract(yestPrice)).divide(yestPrice, 2, BigDecimal.ROUND_DOWN).multiply(oneHundred);
        if (increaseRate.compareTo(BigDecimal.ZERO) < 0) {
            vo.setIncreaseRate(new StringBuffer("-").append(increaseRate.toPlainString()).append("%").toString());
        } else {
            vo.setIncreaseRate(new StringBuffer(increaseRate.toPlainString()).append("%").toString());
        }
        // 涨停价：昨日收盘价*（1+最高报价比例）
        vo.setIncreaseLimitPrice(yestPrice.multiply((one.add(divideOneHundred(product.getHighestQuotedPrice()))))
                .divide(BigDecimal.ONE,2,BigDecimal.ROUND_DOWN).setScale(2));
        // 跌停价：昨日收盘价*（1-最低报价比例）
        vo.setFallLimitPrice(yestPrice.multiply((one.subtract(divideOneHundred(product.getLowestQuotedPrice()))))
                .divide(BigDecimal.ONE,2,BigDecimal.ROUND_DOWN).setScale(2));
        return vo;
    }

    private BigDecimal divideOneHundred(BigDecimal val) {
        return val.divide(oneHundred);
    }
}