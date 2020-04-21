package com.baibei.shiyi.trade.service.impl;

import com.alibaba.fastjson.JSON;
import com.baibei.component.redis.util.RedisUtil;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import com.baibei.shiyi.common.tool.constants.RedisConstant;
import com.baibei.shiyi.common.tool.utils.MapUtil;
import com.baibei.shiyi.trade.dao.TradeConfigMapper;
import com.baibei.shiyi.trade.model.TradeConfig;
import com.baibei.shiyi.trade.service.ITradeConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;
import java.util.Map;


/**
 * @author: uqing
 * @date: 2019/12/20 16:18:12
 * @description: TradeConfig服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TradeConfigServiceImpl extends AbstractService<TradeConfig> implements ITradeConfigService {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private TradeConfigMapper tblTraTradeConfigMapper;

    @Override
    public TradeConfig find() {
        Condition condition = buildValidCondition(TradeConfig.class);
        return findOneByCondition(condition);
    }

    @Override
    public TradeConfig findFromCache() {
        Map<String, Object> tradeConfigCache = redisUtil.hgetAll(RedisConstant.TRADE_CONFIG);
        if (tradeConfigCache != null) {
            return JSON.parseObject(JSON.toJSONString(tradeConfigCache), TradeConfig.class);
        } else {
            TradeConfig tradeConfig = find();
            if (tradeConfig != null) {
                redisUtil.hsetAll(RedisConstant.TRADE_CONFIG, MapUtil.objectToMap(tradeConfig));
            }
            return tradeConfig;
        }
    }

    @Override
    public TradeConfig getConfig() {
        List<TradeConfig> tradeConfigList = this.findAll();
        TradeConfig tradeConfig;
        if (tradeConfigList.isEmpty()) {
            tradeConfig = new TradeConfig();
            return tradeConfig;
        }
        if (tradeConfigList.size() > 1) {
            tradeConfig = new TradeConfig();
            return tradeConfig;
        }
        return tradeConfigList.get(0);
    }
}
