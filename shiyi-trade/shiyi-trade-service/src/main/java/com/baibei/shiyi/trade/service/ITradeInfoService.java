package com.baibei.shiyi.trade.service;

import com.baibei.shiyi.trade.common.dto.TradeInfoDto;
import com.baibei.shiyi.trade.common.vo.PCTradeInfoVo;
import com.baibei.shiyi.trade.common.vo.TradeInfoVo;

/**
 * @author: 会跳舞的机器人
 * @date: 2020/1/7 14:45
 * @description:
 */
public interface ITradeInfoService {

    PCTradeInfoVo pcTradeInfoVo(TradeInfoDto dto);

    TradeInfoVo tradeInfo(TradeInfoDto dto);
}