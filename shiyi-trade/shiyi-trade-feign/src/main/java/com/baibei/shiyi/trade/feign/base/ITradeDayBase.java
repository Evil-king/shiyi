package com.baibei.shiyi.trade.feign.base;

import com.baibei.shiyi.common.tool.api.ApiResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

public interface ITradeDayBase {

    /**
     * 是否是交易日
     *
     * @return
     */
    @RequestMapping("/isTradeDay")
    ApiResult isTradeDay(@RequestParam("day") Date day);

}
