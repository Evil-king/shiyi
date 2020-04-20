package com.baibei.shiyi.admin.modules.trade.web;

import com.alibaba.fastjson.JSONObject;
import com.baibei.shiyi.admin.modules.security.utils.SecurityUtils;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.trade.feign.bean.dto.RiskTradeDto;
import com.baibei.shiyi.trade.feign.client.admin.IAdminRiskTradeFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/risk/trade")
@Slf4j
public class RiskTradeController {

    @Autowired
    private IAdminRiskTradeFeign riskTradeFeign;

    @PostMapping(path = "/save")
    @PreAuthorize("hasAnyRole(@authorityExpression.edit('TEMPORANY_STOP'))")
    public ApiResult<RiskTradeDto> save(@RequestBody RiskTradeDto riskTradeDto) {
        riskTradeDto.setCreator(SecurityUtils.getUsername());
        return riskTradeFeign.save(riskTradeDto);
    }

    @PostMapping(path = "/get")
    @PreAuthorize("hasAnyRole(@authorityExpression.find('TEMPORANY_STOP'))")
    public ApiResult get() {
        ApiResult<RiskTradeDto> result = riskTradeFeign.get();
        log.info("当前的json数据为{}", JSONObject.toJSONString(result));
        return result;
    }
}
