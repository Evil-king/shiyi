package com.baibei.shiyi.trade.feign.service;

import com.baibei.component.redis.util.RedisUtil;
import com.baibei.shiyi.common.tool.constants.RedisConstant;
import com.baibei.shiyi.common.tool.utils.JacksonUtil;
import com.baibei.shiyi.trade.feign.bean.vo.TradeDayVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/12/30 10:00
 * @description:
 */
@Service
public class TradeTimeService {
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 当前是否为交易时间
     *
     * @return true=交易时间；false=非交易时间
     */
    public boolean isTradeTime() {
        String key = RedisConstant.TRADE_TRADE_TIME;
        String tradeDayFlag = redisUtil.get(key);
        return !StringUtils.isEmpty(tradeDayFlag) && "1".equals(tradeDayFlag);
    }

    /**
     * 当前是否已闭市
     *
     * @return true=已闭市；false=未闭市
     */
    public boolean isCloseTime() {
        String key = RedisConstant.TRADE_CLOSE;
        String tradeCloseFlag = redisUtil.get(key);
        return !StringUtils.isEmpty(tradeCloseFlag) && "1".equals(tradeCloseFlag);
    }

    /**
     * 获取交易日配置
     *
     * @return
     */
    public List<TradeDayVo> getTradeDayConfig() {
        String key = RedisConstant.TRADE_TRADE_DAY;
        String val = redisUtil.get(key);
        if (StringUtils.isEmpty(val)) {
            return null;
        }
        return JacksonUtil.jsonToBeanList(val, TradeDayVo.class);
    }
}
