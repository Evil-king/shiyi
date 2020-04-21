package com.baibei.shiyi.trade.dao;

import com.baibei.shiyi.common.core.mybatis.MyMapper;
import com.baibei.shiyi.trade.model.TradeDay;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface TradeDayMapper extends MyMapper<TradeDay> {
    List<Date> listTradeDay(Map<String, Object> map);

    List<TradeDay> selectByDate(@Param("date") String date);
}