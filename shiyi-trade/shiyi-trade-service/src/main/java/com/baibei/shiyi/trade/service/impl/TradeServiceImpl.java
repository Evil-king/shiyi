package com.baibei.shiyi.trade.service.impl;

import com.baibei.shiyi.account.feign.bean.dto.ChangeAmountDto;
import com.baibei.shiyi.account.feign.client.AccountFeign;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.enumeration.FreezingAmountTradeTypeEnum;
import com.baibei.shiyi.common.tool.enumeration.HoldResourceEnum;
import com.baibei.shiyi.common.tool.enumeration.TradeMoneyTradeTypeEnum;
import com.baibei.shiyi.common.tool.exception.SystemException;
import com.baibei.shiyi.common.tool.utils.NoUtil;
import com.baibei.shiyi.trade.common.bo.*;
import com.baibei.shiyi.trade.common.dto.TradeDeListDto;
import com.baibei.shiyi.trade.common.dto.TradeListDto;
import com.baibei.shiyi.trade.model.EntrustOrder;
import com.baibei.shiyi.trade.service.IDealOrderService;
import com.baibei.shiyi.trade.service.IEntrustOrderService;
import com.baibei.shiyi.trade.service.IHoldPostionChangeService;
import com.baibei.shiyi.trade.service.ITradeService;
import com.baibei.shiyi.trade.utils.TradeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: 会跳舞的机器人
 * @date: 2020/1/6 14:44
 * @description: 挂摘牌事务处理层
 */
@Service
@Slf4j
public class TradeServiceImpl implements ITradeService {
    @Autowired
    private IEntrustOrderService entrustOrderService;
    @Autowired
    private AccountFeign accountFeign;
    @Autowired
    private IHoldPostionChangeService holdPostionChangeService;
    @Autowired
    private IDealOrderService dealOrderService;
    @Autowired
    private TradeUtil tradeUtil;


    @Override
    @Transactional(rollbackFor = Throwable.class)
    public EntrustOrderBo doListBuy(TradeListDto dto, BigDecimal totalCost, BigDecimal fee, String entrustNo, String customerName) {
        // step1.创建委托单
        Date entrustTime = new Date();
        Integer anonymousFlag = dto.getAnonymousFlag() == null ? 0 : dto.getAnonymousFlag();// 默认不匿名
        ListBo listBo = ListBo.builder().entrustNo(entrustNo).customerNo(dto.getCustomerNo()).customerName(customerName)
                .productTradeNo(dto.getProductTradeNo()).price(new BigDecimal(dto.getPrice()))
                .count(dto.getCount()).anonymousFlag(anonymousFlag.byteValue()).direction(Constants.TradeDirection.BUY)
                .status(Constants.TransactionStatus.SUCCESS).changeAmount(totalCost).buyFee(fee).entrustTime(entrustTime).build();
        entrustOrderService.save(listBo);
        // step2.冻结客户资金
        ChangeAmountDto changeAmountDto = new ChangeAmountDto();
        changeAmountDto.setCustomerNo(dto.getCustomerNo());
        changeAmountDto.setChangeAmount(totalCost);
        changeAmountDto.setOrderNo(entrustNo);
        changeAmountDto.setTradeType(FreezingAmountTradeTypeEnum.LIST_BUY_FREEZE.getCode());
        changeAmountDto.setReType(Constants.Retype.OUT);
        ApiResult accountApiResult = accountFeign.frozenAmount(changeAmountDto);
        // step3.修改委托单状态
        if (accountApiResult.hasFail()) {
            log.info("客户{}挂牌买入失败，accountApiResult={}", dto.getCustomerNo(), accountApiResult.toString());
            throw new SystemException("挂牌买入失败");
        }
        return EntrustOrderBo.builder().entrustNo(entrustNo).entrustTime(entrustTime).build();
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public EntrustOrderBo doListSell(TradeListDto dto, String entrustNo, String customerName) {
        String customerNo = dto.getCustomerNo();
        String productTradeNo = dto.getProductTradeNo();
        BigDecimal price = new BigDecimal(dto.getPrice());
        Integer count = dto.getCount();
        Integer anonymousFlag = dto.getAnonymousFlag() == null ? 0 : dto.getAnonymousFlag();// 默认不匿名
        // 创建委托单
        Date entrustTime = new Date();
        ListBo listBo = ListBo.builder().entrustNo(entrustNo).customerNo(customerNo).customerName(customerName).productTradeNo(productTradeNo).price(price)
                .count(count).anonymousFlag(anonymousFlag.byteValue()).direction(Constants.TradeDirection.SELL)
                .status(Constants.TransactionStatus.SUCCESS).changeAmount(new BigDecimal(count)).entrustTime(entrustTime).build();
        entrustOrderService.save(listBo);
        // 冻结客户持仓
        holdPostionChangeService.frozen(customerNo, productTradeNo, count);
        return EntrustOrderBo.builder().entrustNo(entrustNo).entrustTime(entrustTime).build();
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public DealOrderBo delistBuy(EntrustOrder entrustOrder, TradeDeListDto dto, String customerName) {
        String delister = dto.getCustomerNo();
        String beDelister = entrustOrder.getCustomerNo();
        BigDecimal price = entrustOrder.getPrice();
        Integer count = dto.getCount();
        // step1.创建成交单
        String dealNo = NoUtil.getDealNo();
        Date dealTime = new Date();
        BigDecimal delisterFee = tradeUtil.getBuyFee(delister, price, count);
        BigDecimal beDelisterFee = tradeUtil.getSellFee(beDelister, price, count);
        DeListBo bo = DeListBo.builder().dealNo(dealNo).entrustNo(entrustOrder.getEntrustNo())
                .productTradeNo(entrustOrder.getProductTradeNo()).delister(delister).beDelister(beDelister)
                .delisterFee(delisterFee).beDelisterFee(beDelisterFee).direction(Constants.TradeDirection.BUY)
                .price(price).count(count).createTime(dealTime).build();
        dealOrderService.save(bo);
        // step2.修改委托单状态
        entrustOrderService.updateEntrustByDelist(entrustOrder, dto.getCount());
        // step3.解冻扣减被摘方持仓
        ChangeHoldPositionBo holdPositionBo = ChangeHoldPositionBo.builder().customerNo(beDelister).
                productTradeNo(entrustOrder.getProductTradeNo()).count(count).price(price)
                .resource(HoldResourceEnum.SELL.getCode()).resourceNo(dealNo).reType(Constants.Retype.OUT).build();
        holdPostionChangeService.unfreezeAndDeduct(holdPositionBo);
        // step4.增加摘牌方商品持仓单，并且记录明细
        ChangeHoldPositionBo increaseHoldPositionBo = ChangeHoldPositionBo.builder().customerNo(delister).
                productTradeNo(entrustOrder.getProductTradeNo()).count(count).price(price)
                .resource(HoldResourceEnum.BUY.getCode()).resourceNo(dealNo).reType(Constants.Retype.IN).customerName(customerName).build();
        holdPostionChangeService.increase(increaseHoldPositionBo);
        // step5.扣减摘牌方资金 && 增加被摘牌方资金
        BigDecimal buyerCost = price.multiply(new BigDecimal(count)).add(delisterFee);
        BigDecimal sellerGet = price.multiply(new BigDecimal(count)).subtract(beDelisterFee);
        List<ChangeAmountDto> changeAmountDtoList = buildAccountDto(delister, buyerCost, beDelister, sellerGet,
                TradeMoneyTradeTypeEnum.ORDER_BUY.getCode(), TradeMoneyTradeTypeEnum.ORDER_SELL.getCode(), dealNo);
        ApiResult accountResult = accountFeign.changeMoneyList(changeAmountDtoList);
        if (accountResult.hasFail()) {
            log.info("摘牌买入资金操作失败，accountResult={}", accountResult.toString());
            throw new SystemException("摘牌买入资金操作失败");
        }
        DealOrderBo result = DealOrderBo.builder().buyer(beDelister).buyActualAmount(buyerCost).seller(delister)
                .sellActualAmount(sellerGet).dealNo(dealNo).dealTime(dealTime).build();
        return result;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public DealOrderBo delistSell(EntrustOrder entrustOrder, TradeDeListDto dto, String customerName) {
        String delister = dto.getCustomerNo();
        String beDelister = entrustOrder.getCustomerNo();
        BigDecimal price = entrustOrder.getPrice();
        Integer count = dto.getCount();
        // step1.创建成交单
        String dealNo = NoUtil.getDealNo();
        Date dealTime = new Date();
        BigDecimal delisterFee = tradeUtil.getSellFee(delister, price, count);
        BigDecimal beDelisterFee = entrustOrder.getBuyFee().multiply(new BigDecimal(count));
        DeListBo bo = DeListBo.builder().dealNo(dealNo).entrustNo(entrustOrder.getEntrustNo())
                .productTradeNo(entrustOrder.getProductTradeNo()).delister(delister).beDelister(beDelister)
                .delisterFee(delisterFee).beDelisterFee(beDelisterFee).direction(Constants.TradeDirection.SELL)
                .price(price).count(count).createTime(dealTime).build();
        dealOrderService.save(bo);
        // step2.修改委托单状态
        entrustOrderService.updateEntrustByDelist(entrustOrder, dto.getCount());
        // step3.扣减摘牌方商品持仓单
        ChangeHoldPositionBo holdPositionBo = ChangeHoldPositionBo.builder().customerNo(delister).
                productTradeNo(entrustOrder.getProductTradeNo()).count(count).price(price)
                .resource(HoldResourceEnum.SELL.getCode()).resourceNo(dealNo).reType(Constants.Retype.OUT).build();
        holdPostionChangeService.deduct(holdPositionBo);
        // step4.增加被摘牌方商品持仓单，并且记录明细
        ChangeHoldPositionBo increaseHoldPositionBo = ChangeHoldPositionBo.builder().customerNo(beDelister).
                productTradeNo(entrustOrder.getProductTradeNo()).count(count).price(price)
                .resource(HoldResourceEnum.BUY.getCode()).resourceNo(dealNo).reType(Constants.Retype.IN).customerName(customerName).build();
        holdPostionChangeService.increase(increaseHoldPositionBo);
        // step5.增加摘牌方资金 && 扣减被摘牌方资金
        BigDecimal buyerCost = price.multiply(new BigDecimal(count)).add(beDelisterFee);
        BigDecimal sellerGet = price.multiply(new BigDecimal(count)).subtract(delisterFee);
        List<ChangeAmountDto> changeAmountDtoList = buildAccountDto(beDelister, buyerCost, delister, sellerGet,
                TradeMoneyTradeTypeEnum.HANG_ORDER_BUY.getCode(), TradeMoneyTradeTypeEnum.PICK_ORDER_SELL.getCode(), dealNo);
        ApiResult accountResult = accountFeign.dealOrder(changeAmountDtoList);
        if (accountResult.hasFail()) {
            log.info("摘牌卖出资金操作失败，accountResult={}", accountResult.toString());
            throw new SystemException("摘牌卖出资金操作失败");
        }
        DealOrderBo result = DealOrderBo.builder().buyer(beDelister).buyActualAmount(buyerCost).seller(delister)
                .sellActualAmount(sellerGet).dealNo(dealNo).dealTime(dealTime).build();
        return result;
    }


    /**
     * 构建资金操作参数
     *
     * @param buyer
     * @param buyerCost
     * @param seller
     * @param sellerGet
     * @param dealNo
     * @return
     */
    private List<ChangeAmountDto> buildAccountDto(String buyer, BigDecimal buyerCost, String seller, BigDecimal sellerGet,
                                                  String buyerTradeType, String sellTradeType, String dealNo) {
        List<ChangeAmountDto> list = new ArrayList<>();
        // 买方扣减资金
        ChangeAmountDto buyerChangeDto = new ChangeAmountDto();
        buyerChangeDto.setCustomerNo(buyer);
        buyerChangeDto.setChangeAmount(buyerCost);
        buyerChangeDto.setOrderNo(dealNo);
        buyerChangeDto.setTradeType(buyerTradeType);
        buyerChangeDto.setReType(Constants.Retype.OUT);
        list.add(buyerChangeDto);
        // 卖方获得资金
        ChangeAmountDto sellerChangeDto = new ChangeAmountDto();
        sellerChangeDto.setCustomerNo(seller);
        sellerChangeDto.setChangeAmount(sellerGet);
        sellerChangeDto.setOrderNo(dealNo);
        sellerChangeDto.setTradeType(sellTradeType);
        sellerChangeDto.setReType(Constants.Retype.IN);
        list.add(sellerChangeDto);
        return list;
    }
}
