package com.baibei.shiyi.trade.biz;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.trade.model.HoldDetails;
import com.baibei.shiyi.trade.service.IHoldDetailsService;
import com.baibei.shiyi.trade.service.IHoldPositionService;
import com.baibei.shiyi.trade.service.IHoldPostionChangeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/12/27 18:35
 * @description:
 */
@Slf4j
@Component
public class UnlockHolPositionBiz {
    @Autowired
    private IHoldDetailsService holdDetailsService;
    @Autowired
    private IHoldPostionChangeService holdPostionChangeService;

    /**
     * 解锁持仓明细
     *
     * @return
     */
    public ApiResult unlockHoldPosition() {
        List<HoldDetails> holdDetailsList = holdDetailsService.findCanTradeList();
        if (CollectionUtils.isEmpty(holdDetailsList)) {
            log.info("可解锁明细为空");
            return ApiResult.success();
        }
        for (HoldDetails details : holdDetailsList) {
            holdPostionChangeService.unlock(details);
        }
        return ApiResult.success();
    }
}