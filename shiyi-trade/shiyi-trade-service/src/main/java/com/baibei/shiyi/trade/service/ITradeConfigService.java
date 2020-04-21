package com.baibei.shiyi.trade.service;

import com.baibei.shiyi.trade.model.TradeConfig;
import com.baibei.shiyi.common.core.mybatis.Service;


/**
 * @author: uqing
 * @date: 2019/12/20 16:18:12
 * @description: TradeConfig服务接口
 */
public interface ITradeConfigService extends Service<TradeConfig> {

    TradeConfig find();


    TradeConfig findFromCache();

    /**
     * 获取配置
     * @return
     */
    TradeConfig getConfig();
}
