package com.baibei.shiyi.trade.service.impl;

import com.alibaba.fastjson.JSON;
import com.baibei.component.redis.util.RedisUtil;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import com.baibei.shiyi.common.tool.constants.RedisConstant;
import com.baibei.shiyi.common.tool.utils.DateUtil;
import com.baibei.shiyi.trade.dao.TradeDayMapper;
import com.baibei.shiyi.trade.event.SystemRevokePublisher;
import com.baibei.shiyi.trade.model.TradeDay;
import com.baibei.shiyi.trade.service.ITradeDayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/05/27 16:00:10
 * @description: TradeDay服务实现
 */
@Service
@Slf4j
public class TradeDayServiceImpl extends AbstractService<TradeDay> implements ITradeDayService {
    @Autowired
    private TradeDayMapper tblTradeDayMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private SystemRevokePublisher systemRevokePublisher;

    @Override
    public void doScheduler() {
        Date now = new Date();
        List<TradeDay> list = tblTradeDayMapper.selectByDate(DateUtil.yyyyMMddWithLine.get().format(now));
        // step1.设置开休市状态
        setTradeTime2Redis(list, now);
        // step2.缓存最新的交易日配置
        setTradeConfig2Redis(list);
        // step3.设置闭市状态
        boolean closeFlg = setMarketClose2Redis(list, now);
        if (closeFlg) {
            String key = RedisConstant.TRADE_CLOSE_NOTICE;
            String val = redisUtil.get(key);
            if (StringUtils.isEmpty(val) || "0".equals(val)) {
                // 标识为已发送
                redisUtil.set(key, "1");
                redisUtil.expireAt(key, DateUtil.getEndDay());
                // 闭市后发送撤单通知
                systemRevokePublisher.push(DateUtil.yyyyMMddWithLine.get().format(now));
            }
        }
    }

    /**
     * 设置开休市状态至Redis
     *
     * @param list
     * @param now
     * @return
     */
    private boolean setTradeTime2Redis(List<TradeDay> list, Date now) {
        // 先设置为开市
        String key = RedisConstant.TRADE_TRADE_TIME;
        redisUtil.set(key, "0");
        if (CollectionUtils.isEmpty(list)) {
            return false;
        }
        Date startTime, endTime;
        for (TradeDay td : list) {
            startTime = DateUtil.appendHhmmss(td.getStartTime());
            endTime = DateUtil.appendHhmmss(td.getEndTime());
            // 满足条件设置为开市
            if (now.getTime() >= startTime.getTime() && now.getTime() <= endTime.getTime()) {
                redisUtil.set(key, "1");
                return true;
            }
        }
        return false;
    }

    /**
     * 设置闭市状态至Redis
     *
     * @param list
     * @param now
     * @return
     */
    private boolean setMarketClose2Redis(List<TradeDay> list, Date now) {
        String key = RedisConstant.TRADE_CLOSE;
        redisUtil.set(key, "0");
        if (CollectionUtils.isEmpty(list)) {
            redisUtil.set(key, "1");
            return false;
        }
        Date endTime;
        for (TradeDay td : list) {
            endTime = DateUtil.appendHhmmss(td.getEndTime());
            if ("afternoon".equals(td.getPeriod()) && now.getTime() > endTime.getTime()) {
                redisUtil.set(key, "1");
                return true;
            }
        }
        return false;
    }

    /**
     * 设置交易日配置至Redis
     *
     * @param list
     */
    private void setTradeConfig2Redis(List<TradeDay> list) {
        if (!CollectionUtils.isEmpty(list)) {
            String key = RedisConstant.TRADE_TRADE_DAY;
            redisUtil.set(key, JSON.toJSONString(list));
        }
    }

    @Override
    public boolean isTradeDay() {
        return isTradeDay(new Date());
    }

    @Override
    public boolean isTradeDay(Date date) {
        List<TradeDay> list = tblTradeDayMapper.selectByDate(DateUtil.yyyyMMddWithLine.get().format(date));
        return !CollectionUtils.isEmpty(list);
    }

    @Override
    public boolean isTradeTime() {
        String key = RedisConstant.TRADE_TRADE_TIME;
        String val = redisUtil.get(key);
        if (!StringUtils.isEmpty(val) && "1".equals(val)) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public boolean isClose() {
        String key = RedisConstant.TRADE_CLOSE;
        String val = redisUtil.get(key);
        if (!StringUtils.isEmpty(val) && "1".equals(val)) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public Date getAddNTradeDay(int n) {
        return getAddNTradeDay(new Date(), n);
    }


    @Override
    public Date getAddNTradeDay(Date date, int n) {
        int value = Math.abs(n);
        Map<String, Object> param = new HashMap<>();
        param.put("date", date);
        param.put("forward", n > 0 ? 1 : 0);
        param.put("limit", value);
        List<Date> list = tblTradeDayMapper.listTradeDay(param);
        if (CollectionUtils.isEmpty(list) || list.size() < value) {
            return null;
        }
        return list.get(value - 1);
    }
}
