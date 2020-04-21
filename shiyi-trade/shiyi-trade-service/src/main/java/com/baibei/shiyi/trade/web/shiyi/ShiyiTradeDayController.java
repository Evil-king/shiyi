package com.baibei.shiyi.trade.web.shiyi;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.trade.feign.base.ITradeDayBase;
import com.baibei.shiyi.trade.service.ITradeDayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class ShiyiTradeDayController implements ITradeDayBase {
    @Autowired
    private ITradeDayService tradeDayService;

    @Override
    public ApiResult isTradeDay(@RequestParam("day") Date day) {
        return ApiResult.success(tradeDayService.isTradeDay(day));
    }
}
