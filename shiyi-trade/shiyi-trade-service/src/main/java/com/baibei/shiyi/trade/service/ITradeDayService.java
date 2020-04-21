package com.baibei.shiyi.trade.service;

import com.baibei.shiyi.common.core.mybatis.Service;
import com.baibei.shiyi.trade.model.TradeDay;

import java.util.Date;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/05/27 16:00:10
 * @description: TradeDay服务接口
 */
public interface ITradeDayService extends Service<TradeDay> {

    /**
     * 交易日定时任务
     */
    void doScheduler();

    /**
     * 查询当前日期是否是交易日
     *
     * @return
     */
    boolean isTradeDay();

    /**
     * 查看指定时间是否是交易日
     *
     * @param date
     * @return true表示是交易日, false表示非交易日
     */
    boolean isTradeDay(Date date);


    /**
     * 查询当前时间是否可交易时间
     *
     * @return
     */
    boolean isTradeTime();


    /**
     * 是否闭市
     *
     * @return true：已闭市；false：未闭市
     */
    boolean isClose();


    /**
     * 获取当前时间+N之后的交易时间
     * n>0表示取当前时间之后的交易日，n<0表示取当前时间之前的交易日
     *
     * @return
     */
    Date getAddNTradeDay(int n);


    /**
     * 获取指定时间+N之后的交易时间
     * n>0表示取指定时间之后的交易日，n<0表示取指定时间之前的交易日
     *
     * @param date
     * @param n
     * @return
     */
    Date getAddNTradeDay(Date date, int n);
}
