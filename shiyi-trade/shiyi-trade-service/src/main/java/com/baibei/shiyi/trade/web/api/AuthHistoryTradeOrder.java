package com.baibei.shiyi.trade.web.api;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.trade.common.dto.HoldRecordDto;
import com.baibei.shiyi.trade.common.vo.HoldRecordVo;
import com.baibei.shiyi.trade.service.IDealOrderService;
import com.baibei.shiyi.trade.service.IHoldRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 交易历史记录
 */
@RestController
@RequestMapping("/auth/api/trade/historyTrade")
public class AuthHistoryTradeOrder {

    @Autowired
    private IDealOrderService dealOrderService;
    @Autowired
    private IHoldRecordService holdRecordService;

//
//    @PostMapping("/customerHistoryList")
//    public ApiResult<MyPageInfo<DealOrderHistoryVo>> customerHistoryList(@RequestBody DealOrderQueryDTO dealOrderQueryDTO) {
//        return ApiResult.success(dealOrderService.customerHistoryList(dealOrderQueryDTO));
//    }

    @PostMapping("/customerHistoryList")
    public ApiResult<MyPageInfo<HoldRecordVo>> customerHistoryList(@RequestBody HoldRecordDto holdRecordDto) {
        return ApiResult.success(holdRecordService.customerHistoryList(holdRecordDto));
    }
}
