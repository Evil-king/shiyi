package com.baibei.shiyi.trade.web.shiyi;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.trade.biz.UnlockHolPositionBiz;
import com.baibei.shiyi.trade.service.ITradeDayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: 会跳舞的机器人
 * @date: 2020/1/7 15:46
 * @description: 定时任务
 */
@RestController
public class ShiyiSchedulerController {
    @Autowired
    private ITradeDayService tradeDayService;
    @Autowired
    private UnlockHolPositionBiz unlockHolPositionBiz;

    /**
     * 交易日定时任务
     *
     * @return
     */
    @GetMapping("/shiyi/trade/tradeDay")
    public ApiResult tradeDay() {
        tradeDayService.doScheduler();
        return ApiResult.success();
    }

    /**
     * T+N解锁持仓
     *
     * @return
     */
    @GetMapping("/shiyi/trade/unlockHoldPosition")
    public ApiResult<Boolean> unlockHoldPosition() {
        return unlockHolPositionBiz.unlockHoldPosition();
    }
}