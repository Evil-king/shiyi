package com.baibei.shiyi.trade.service;

import com.baibei.shiyi.trade.feign.bean.dto.RiskTradeDto;
import com.baibei.shiyi.trade.model.RiskTrade;
import com.baibei.shiyi.common.core.mybatis.Service;


/**
 * @author: uqing
 * @date: 2019/12/30 14:09:56
 * @description: RiskTrade服务接口
 */
public interface IRiskTradeService extends Service<RiskTrade> {

    void saveRiskTrade(RiskTradeDto riskTradeDto);

    RiskTradeDto get();

    /**
     * 查询当前有效的停盘风控配置
     *
     * @return
     */
    RiskTrade find();

    /**
     * 判断当前是否风控停盘，true=是，false=否
     *
     * @return
     */
    boolean isRiskTrade();
}
