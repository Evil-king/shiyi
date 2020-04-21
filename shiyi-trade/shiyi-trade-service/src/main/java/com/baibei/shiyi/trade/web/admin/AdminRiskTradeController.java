package com.baibei.shiyi.trade.web.admin;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.utils.BeanUtil;
import com.baibei.shiyi.common.tool.utils.CollectionUtils;
import com.baibei.shiyi.trade.feign.bean.dto.RiskTradeDto;
import com.baibei.shiyi.trade.feign.client.admin.IAdminRiskTradeFeign;
import com.baibei.shiyi.trade.model.RiskTrade;
import com.baibei.shiyi.trade.service.IRiskTradeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;


/**
 * 交易风控管理
 */
@RestController
@RequestMapping("/shiyi/admin/risk/trade")
@Slf4j
public class AdminRiskTradeController implements IAdminRiskTradeFeign {

    @Autowired
    private IRiskTradeService riskTradeService;


    // step 暂停交易时,已经失效。而且如果失效的话，要不要更改状态

    // step 开始交易时
    public ApiResult save(@Validated @RequestBody RiskTradeDto riskTradeDto) {
        this.riskTradeService.saveRiskTrade(riskTradeDto);
        return ApiResult.success();
    }

    public ApiResult get() {
        Condition constants = new Condition(RiskTrade.class);
        riskTradeService.buildValidCriteria(constants);
        List<RiskTrade> riskTradeList = riskTradeService.findByCondition(constants);
        // step 1 查询
        if (CollectionUtils.isEmpty(riskTradeList)) {
            log.info("当前查询交易风控的数据为空");
            return ApiResult.success();
        }
        // step 2
        if (riskTradeList.size() > 1) {
            log.info("当前查询交易风控的数据为长度{}", riskTradeList.size());
            return ApiResult.error("获取交易风控失败");
        }
        RiskTrade riskTrade = riskTradeList.get(0);
        RiskTradeDto tradeDto = BeanUtil.copyProperties(riskTrade, RiskTradeDto.class);
        tradeDto.setOpenFlag(riskTrade.getOpenFlag().toString());
        return ApiResult.success(tradeDto);
    }

}
