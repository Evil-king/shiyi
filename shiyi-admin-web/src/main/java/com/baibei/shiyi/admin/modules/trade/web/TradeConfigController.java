package com.baibei.shiyi.admin.modules.trade.web;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.trade.feign.bean.dto.AdminTradeConfigDto;
import com.baibei.shiyi.trade.feign.client.admin.IAdminTradeConfigFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/trade/config")
public class TradeConfigController {
    @Autowired
    private IAdminTradeConfigFeign adminTradeConfigFeign;

    @PostMapping(path = "/getConfig")
    @PreAuthorize("hasAnyRole(@authorityExpression.find('GLOBAL_PARAMS'))")
    public ApiResult<AdminTradeConfigDto> getConfig() {
        return adminTradeConfigFeign.getConfig();
    }

    @PostMapping(path = "/save")
    @PreAuthorize("hasAnyRole(@authorityExpression.edit('GLOBAL_PARAMS'))")
    public ApiResult save(@RequestBody AdminTradeConfigDto adminTradeConfigDto) {
        return adminTradeConfigFeign.save(adminTradeConfigDto);
    }
}
