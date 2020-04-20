package com.baibei.shiyi.cash.feign.client.shiyi;

import com.baibei.shiyi.cash.feign.base.dto.WithdrawForBank1312Dto;
import com.baibei.shiyi.cash.feign.base.vo.WithdrawForBank1312Vo;
import com.baibei.shiyi.cash.feign.client.hystrix.ShiyiWithdrawHystrix;
import com.baibei.shiyi.common.tool.api.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@FeignClient(value = "${shiyi-cash:shiyi-cash}", path = "/shiyi/withdraw", fallbackFactory = ShiyiWithdrawHystrix.class)
public interface IShiyiWithdrawFeign {

    /**
     * 银行-->交易网 1312
     * @return
     */
    @PostMapping("/withdrawForBank1312")
    @ResponseBody
    ApiResult<WithdrawForBank1312Vo> withdrawForBank1312(WithdrawForBank1312Dto withdrawForBank1312Dto);

    /**
     * applySchedule1325
     * 交易网--->银行(定时任务--1325接口)
     */
    @PostMapping("/applySchedule1325")
    @ResponseBody
    ApiResult applySchedule1325();

    /**
     * 定时扫描已经审核过的出金订单，调用1318接口进行出金
     * @return
     */
    @PostMapping("/applySchedule1318")
    @ResponseBody
    ApiResult applySchedule1318();

    /**
     * 定时扫描处理银行端发起的出金订单，调用1317接口
     * @return
     */
    @PostMapping("/applySchedule1317")
    @ResponseBody
    ApiResult applySchedule1317();

    /**
     * 签退的时候将提现申请中的订单置为审核失败
     * @return
     */
    @PostMapping("/operatorReview")
    @ResponseBody
    ApiResult operatorReview();

    /**
     * 定时扫描处理银行端发起的出金订单，调用福清315002接口
     * @return
     */
    @PostMapping("/applyScheduleFuqing315002")
    @ResponseBody
    ApiResult applyScheduleFuqing315002();

    /**
     * 定时扫描查询处理中的订单，查看银行处理结果
     * @return
     */
    @PostMapping("/applyScheduleFuqing315003")
    @ResponseBody
    ApiResult applyScheduleFuqing315003();
}
