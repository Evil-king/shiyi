package com.baibei.shiyi.trade.biz;

import com.baibei.component.redis.util.DistributedLock;
import com.baibei.component.rocketmq.core.util.RocketMQUtil;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.api.ResultEnum;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.constants.RedisConstant;
import com.baibei.shiyi.common.tool.exception.SystemException;
import com.baibei.shiyi.common.tool.utils.IdWorker;
import com.baibei.shiyi.common.tool.utils.JacksonUtil;
import com.baibei.shiyi.common.tool.utils.NoUtil;
import com.baibei.shiyi.common.tool.validator2.FluentValidator;
import com.baibei.shiyi.settlement.feign.bean.message.SettlementMetadataMsg;
import com.baibei.shiyi.trade.biz.validator.*;
import com.baibei.shiyi.trade.common.bo.DealOrderBo;
import com.baibei.shiyi.trade.common.bo.EntrustOrderBo;
import com.baibei.shiyi.trade.common.bo.TradeValidateBo;
import com.baibei.shiyi.trade.common.dto.TradeDeListDto;
import com.baibei.shiyi.trade.common.dto.TradeListDto;
import com.baibei.shiyi.trade.model.EntrustOrder;
import com.baibei.shiyi.trade.rocketmq.message.PushQuotationMsg;
import com.baibei.shiyi.trade.service.IEntrustOrderService;
import com.baibei.shiyi.trade.service.IHoldDetailsService;
import com.baibei.shiyi.trade.service.ITradeService;
import com.baibei.shiyi.trade.thread.CalculationCostThread;
import com.baibei.shiyi.trade.utils.TradeUtil;
import com.baibei.shiyi.user.feign.bean.vo.CustomerVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/12/26 18:04
 * @description: 挂摘牌业务逻辑
 */
@Component
@Slf4j
public class TradeBiz {
    /**
     * 应用名称
     */
    @Value("${spring.application.name}")
    private String applicationName;
    /**
     * 交易资金清算
     */
    @Value("${rocketmq.settlement.clean.topics}")
    private String settlementCleanTopics;
    /**
     * 推送行情消息
     */
    @Value("${rocketmq.trade.pushQuotation.topics}")
    private String pushQuotationTopics;
    @Autowired
    private ITradeService tradeService;
    @Autowired
    private IHoldDetailsService holdDetailsService;
    @Autowired
    private TradeUtil tradeUtil;
    @Autowired
    private DistributedLock distributedLock;
    @Autowired
    private IEntrustOrderService entrustOrderService;
    @Autowired
    private RiskTradeStatusValidator riskTradeStatusValidator;
    @Autowired
    private TradeTimeValidator tradeTimeValidator;
    @Autowired
    private BuyerTradeStatusValidator buyerTradeStatusValidator;
    @Autowired
    private BuyerBalanceValidator buyerBalanceValidator;
    @Autowired
    private CustomerSignValidator customerSignValidator;
    @Autowired
    private SellerTradeStatusValidator sellerTradeStatusValidator;
    @Autowired
    private SellerHoldPositionValidator sellerHoldPositionValidator;
    @Autowired
    private EntrustOrderValidator entrustOrderValidator;
    @Autowired
    private SellCountValidator sellCountValidator;
    @Autowired
    private TradeCountValidator tradeCountValidator;
    @Autowired
    private RocketMQUtil rocketMQUtil;
    @Autowired
    private ProductTradeStatusValidator productTradeStatusValidator;
    @Autowired
    private PriceValidator priceValidator;
    /**
     * 异步计算持仓成本线程池
     */
    private ExecutorService costExecutorService = Executors.newFixedThreadPool(5);


    /**
     * 挂牌买入
     *
     * @param dto
     * @return
     */
    public ApiResult listBuy(TradeListDto dto) {
        long start = System.currentTimeMillis();
        String customerNo = dto.getCustomerNo();
        String productTradeNo = dto.getProductTradeNo();
        BigDecimal price = new BigDecimal(dto.getPrice());
        Integer count = dto.getCount();
        // 计算买入总资金
        BigDecimal fee = tradeUtil.getSingleBuyFee(customerNo, price);
        BigDecimal cost = price.multiply(new BigDecimal(count));
        BigDecimal totalFee = fee.multiply(new BigDecimal(count));
        BigDecimal totalCost = cost.add(totalFee);
        // step1.业务规则校验
        TradeValidateBo tradeValidateBo = TradeValidateBo.builder().customerNo(customerNo)
                .productTradeNo(productTradeNo).count(count).totalCost(totalCost).price(new BigDecimal(dto.getPrice())).build();
        FluentValidator fluentValidator = FluentValidator.checkAll()
                .on(tradeTimeValidator)  // 判断交易时间
                .on(riskTradeStatusValidator)   // 判断交易风控状态
                .on(tradeValidateBo, productTradeStatusValidator)    // 交易商品状态判断
                .on(tradeValidateBo, tradeCountValidator)    // 判断交易商品数量是否正确
                .on(tradeValidateBo, priceValidator)    // 报价是否合理校验
                .on(customerNo, customerSignValidator)  // 判断客户签约开户状态
                .on(buyerTradeStatusValidator)  // 判断客户交易状态
                .on(tradeValidateBo, buyerBalanceValidator) // 判断买入资金是否足够
                .doValidate();
        CustomerVo customerVo = fluentValidator.getContext().getClazz("customerVo");
        if (customerVo == null) {
            throw new SystemException("客户信息为空");
        }
        // step2.挂牌买入逻辑
        String entrustNo = NoUtil.getEntrustNo();
        EntrustOrderBo bo = tradeService.doListBuy(dto, totalCost, fee, entrustNo, customerVo.getRealName());
        // step3.MQ异步推送行情数据
        sendQuotationMsg(Constants.QuotationOperateType.HANG_BUY,
                productTradeNo, price, count, entrustNo, bo.getEntrustTime());
        log.info("listBuy time consuming {} ms", (System.currentTimeMillis() - start));
        return ApiResult.success();
    }


    /**
     * 挂牌卖出
     *
     * @param dto
     * @return
     */
    public ApiResult listSell(TradeListDto dto) {
        long start = System.currentTimeMillis();
        // step1.业务规则校验
        TradeValidateBo tradeValidateBo = TradeValidateBo.builder().customerNo(dto.getCustomerNo())
                .productTradeNo(dto.getProductTradeNo()).count(dto.getCount()).price(new BigDecimal(dto.getPrice())).build();
        FluentValidator fluentValidator = FluentValidator.checkAll()
                .on(tradeTimeValidator)  // 判断交易时间
                .on(riskTradeStatusValidator)   // 判断交易风控状态
                .on(tradeValidateBo, productTradeStatusValidator)    // 交易商品状态判断
                .on(tradeValidateBo, tradeCountValidator)    // 判断交易商品数量是否正确
                .on(tradeValidateBo, sellCountValidator)     // 判断最大可卖数量
                .on(tradeValidateBo, priceValidator)    // 报价是否合理校验
                .on(dto.getCustomerNo(), customerSignValidator)  // 判断客户签约开户状态
                .on(sellerTradeStatusValidator)  // 判断客户交易状态
                .on(tradeValidateBo, sellerHoldPositionValidator) // 判断持仓是否足够
                .doValidate();
        CustomerVo customerVo = fluentValidator.getContext().getClazz("customerVo");
        if (customerVo == null) {
            throw new SystemException("客户信息为空");
        }
        // step2.具体卖出逻辑
        String entrustNo = NoUtil.getEntrustNo();
        EntrustOrderBo bo = tradeService.doListSell(dto, entrustNo, customerVo.getRealName());
        // step3.MQ异步推送行情数据
        sendQuotationMsg(Constants.QuotationOperateType.HANG_SELL, dto.getProductTradeNo(),
                new BigDecimal(dto.getPrice()), dto.getCount(), entrustNo, bo.getEntrustTime());
        log.info("listSell time consuming {} ms", (System.currentTimeMillis() - start));
        return ApiResult.success();
    }


    /**
     * 摘牌买入/卖出
     *
     * @param dto
     * @return
     */
    public ApiResult delist(TradeDeListDto dto) {
        // 获取判断委托单成交状态
        EntrustOrder entrustOrder = entrustOrderService.findByOrderNo(dto.getEntrustNo());
        if (entrustOrder == null) {
            throw new SystemException("委托单不存在");
        }
        // 摘挂卖单，即摘牌买入
        if (Constants.TradeDirection.SELL.equals(entrustOrder.getDirection())) {
            return delistBuy(entrustOrder, dto);
            // 摘挂买单，即摘牌卖出
        } else if (Constants.TradeDirection.BUY.equals(entrustOrder.getDirection())) {
            return delistSell(entrustOrder, dto);
        } else {
            throw new SystemException("错误的成交方向");
        }
    }

    /**
     * 摘牌买入
     *
     * @param entrustOrder
     * @param dto
     * @return
     */
    private ApiResult delistBuy(EntrustOrder entrustOrder, TradeDeListDto dto) {
        long start = System.currentTimeMillis();
        BigDecimal cost = entrustOrder.getPrice().multiply(new BigDecimal(dto.getCount()));
        BigDecimal totalCost = cost.add(tradeUtil.getBuyFee(dto.getCustomerNo(), entrustOrder.getPrice(), dto.getCount()));
        // step1.业务规则校验
        TradeValidateBo tradeValidateBo = TradeValidateBo.builder().customerNo(dto.getCustomerNo())
                .productTradeNo(entrustOrder.getProductTradeNo()).totalCost(totalCost).count(dto.getCount())
                .entrustOrder(entrustOrder).build();
        FluentValidator fluentValidator = FluentValidator.checkAll()
                .on(tradeTimeValidator)  // 判断交易时间
                .on(riskTradeStatusValidator)   // 判断交易风控状态
                .on(tradeValidateBo, productTradeStatusValidator)    // 交易商品状态判断
                .on(tradeValidateBo, tradeCountValidator)    // 判断交易商品数量是否正确
                .on(dto.getCustomerNo(), customerSignValidator)  // 判断客户签约开户状态
                .on(buyerTradeStatusValidator)  // 判断客户交易状态
                .on(tradeValidateBo, entrustOrderValidator)  // 判断委托单是否合法
                .on(tradeValidateBo, buyerBalanceValidator) // 判断买入资金是否足够
                .doValidate();
        CustomerVo customerVo = fluentValidator.getContext().getClazz("customerVo");
        if (customerVo == null) {
            throw new SystemException("客户信息为空");
        }
        // 获取锁
        String key = MessageFormat.format(RedisConstant.TRADE_ENTRUST_LOCK, entrustOrder.getEntrustNo());
        String value = IdWorker.get32UUID();
        boolean lockFlag = distributedLock.getLock(key, value, 5);
        if (!lockFlag) {
            log.info("委托单【{}】获取锁失败", entrustOrder.getEntrustNo());
            return ApiResult.build(ResultEnum.DELIST_DEAL);
        }
        try {
            // step2.摘牌买入逻辑
            DealOrderBo bo = tradeService.delistBuy(entrustOrder, dto, customerVo.getRealName());
            // step3.MQ异步发送资金清算消息
            sendSettlementMsg(bo);
            // step4.MQ异步推送行情数据
            sendQuotationMsg(Constants.QuotationOperateType.TRADE_SELL, entrustOrder.getProductTradeNo(),
                    entrustOrder.getPrice(), dto.getCount(), bo.getDealNo(), bo.getDealTime());
            // step5.线程池异步计算成本价
            costExecutorService.submit(new CalculationCostThread(dto.getCustomerNo(), entrustOrder.getProductTradeNo(), holdDetailsService));
        } catch (Exception e) {
            log.error("摘牌买入异常", e);
            throw e;
        } finally {
            // 释放锁
            distributedLock.releaseLock(key, value);
        }
        log.info("delistBuy time consuming {} ms", (System.currentTimeMillis() - start));
        return ApiResult.success();
    }

    /**
     * 摘牌卖出
     *
     * @param entrustOrder
     * @param dto
     * @return
     */
    private ApiResult delistSell(EntrustOrder entrustOrder, TradeDeListDto dto) {
        long start = System.currentTimeMillis();
        // step1.业务规则校验
        TradeValidateBo tradeValidateBo = TradeValidateBo.builder().customerNo(dto.getCustomerNo())
                .productTradeNo(entrustOrder.getProductTradeNo()).count(dto.getCount()).entrustOrder(entrustOrder).build();
        FluentValidator fluentValidator = FluentValidator.checkAll()
                .on(tradeTimeValidator)  // 判断交易时间
                .on(riskTradeStatusValidator)   // 判断交易风控状态
                .on(tradeValidateBo, productTradeStatusValidator)    // 交易商品状态判断
                .on(tradeValidateBo, tradeCountValidator)    // 判断交易商品数量是否正确
                .on(tradeValidateBo, sellCountValidator)     // 判断最大可卖数量
                .on(dto.getCustomerNo(), customerSignValidator)  // 判断客户签约开户状态
                .on(sellerTradeStatusValidator)  // 判断客户交易状态
                .on(tradeValidateBo, entrustOrderValidator)  // 判断委托单是否合法
                .on(tradeValidateBo, sellerHoldPositionValidator) // 判断持仓是否足够
                .doValidate();
        CustomerVo customerVo = fluentValidator.getContext().getClazz("customerVo");
        if (customerVo == null) {
            throw new SystemException("客户信息为空");
        }
        // 获取锁
        String key = MessageFormat.format(RedisConstant.TRADE_ENTRUST_LOCK, entrustOrder.getEntrustNo());
        String value = IdWorker.get32UUID();
        boolean lockFlag = distributedLock.getLock(key, value, 5);
        if (!lockFlag) {
            log.info("委托单【{}】获取锁失败", entrustOrder.getEntrustNo());
            return ApiResult.build(ResultEnum.DELIST_DEAL);
        }
        try {
            // step2.摘牌卖出逻辑
            DealOrderBo bo = tradeService.delistSell(entrustOrder, dto, entrustOrder.getCustomerName());
            // step3.MQ异步发送资金清算消息
            sendSettlementMsg(bo);
            // step4.MQ异步推送行情数据
            sendQuotationMsg(Constants.QuotationOperateType.TRADE_BUY, entrustOrder.getProductTradeNo(),
                    entrustOrder.getPrice(), dto.getCount(), bo.getDealNo(), bo.getDealTime());
            // step5.线程异步计算成本价
            costExecutorService.submit(new CalculationCostThread(entrustOrder.getCustomerNo(), entrustOrder.getProductTradeNo(), holdDetailsService));
        } catch (Exception e) {
            log.error("摘牌买入异常", e);
            throw e;
        } finally {
            // 释放锁
            distributedLock.releaseLock(key, value);
        }
        log.info("delistSell time consuming {} ms", (System.currentTimeMillis() - start));
        return ApiResult.success();
    }

    /**
     * 发送成交资金清算消息
     *
     * @param bo
     */
    private void sendSettlementMsg(DealOrderBo bo) {
        List<SettlementMetadataMsg> settlementMetadataMsgList = new ArrayList();
        // 买入方资金支出（含手续费）
        SettlementMetadataMsg customerMsg = new SettlementMetadataMsg();
        customerMsg.setCustomerNo(bo.getBuyer());
        customerMsg.setAmount(bo.getBuyActualAmount());
        customerMsg.setTransferBizType(Constants.TransferBizType.TRADE);
        customerMsg.setRetype(Constants.Retype.OUT);
        customerMsg.setOrderNo(bo.getDealNo());
        customerMsg.setEventTime(bo.getDealTime());
        customerMsg.setApplicationName(applicationName);
        settlementMetadataMsgList.add(customerMsg);
        // 卖出方资金收入（不含手续费）
        SettlementMetadataMsg distributorMsg = new SettlementMetadataMsg();
        distributorMsg.setCustomerNo(bo.getSeller());
        distributorMsg.setAmount(bo.getSellActualAmount());
        distributorMsg.setTransferBizType(Constants.TransferBizType.TRADE);
        distributorMsg.setRetype(Constants.Retype.IN);
        distributorMsg.setOrderNo(bo.getDealNo());
        distributorMsg.setEventTime(bo.getDealTime());
        distributorMsg.setApplicationName(applicationName);
        settlementMetadataMsgList.add(distributorMsg);
        String msg = JacksonUtil.beanToJson(settlementMetadataMsgList);
        rocketMQUtil.sendMsg(settlementCleanTopics, msg, bo.getDealNo());
    }

    /**
     * 发送推送行情消息
     *
     * @param operateType
     * @param productTradeNo
     * @param price
     * @param count
     * @param orderNo
     */
    private void sendQuotationMsg(String operateType, String productTradeNo, BigDecimal price,
                                  Integer count, String orderNo, Date occurTime) {
        PushQuotationMsg quotationMsg = new PushQuotationMsg(operateType, productTradeNo, price, count, orderNo, occurTime);
        String msg = JacksonUtil.beanToJson(quotationMsg);
        rocketMQUtil.sendMsg(pushQuotationTopics, msg, orderNo);
    }
}