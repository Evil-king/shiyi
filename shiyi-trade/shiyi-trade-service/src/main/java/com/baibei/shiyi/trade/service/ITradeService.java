package com.baibei.shiyi.trade.service;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.trade.common.bo.DealOrderBo;
import com.baibei.shiyi.trade.common.bo.EntrustOrderBo;
import com.baibei.shiyi.trade.common.dto.TradeDeListDto;
import com.baibei.shiyi.trade.common.dto.TradeListDto;
import com.baibei.shiyi.trade.model.EntrustOrder;

import java.math.BigDecimal;

/**
 * @author: 会跳舞的机器人
 * @date: 2020/1/6 14:44
 * @description:
 */
public interface ITradeService {

    /**
     * 挂牌买入
     *
     * @param dto
     * @param totalCost
     * @param customerName
     * @return
     */
    EntrustOrderBo doListBuy(TradeListDto dto, BigDecimal totalCost, BigDecimal fee, String entrustNo, String customerName);

    /**
     * 挂牌卖出
     *
     * @param dto
     * @param customerName
     * @return
     */
    EntrustOrderBo doListSell(TradeListDto dto, String entrustNo, String customerName);

    /**
     * 摘牌买入
     *
     * @param entrustOrder
     * @param dto
     */
    DealOrderBo delistBuy(EntrustOrder entrustOrder, TradeDeListDto dto, String customerName);

    /**
     * 摘牌卖出
     *
     * @param entrustOrder
     * @param dto
     * @return
     */
    DealOrderBo delistSell(EntrustOrder entrustOrder, TradeDeListDto dto, String customerName);
}