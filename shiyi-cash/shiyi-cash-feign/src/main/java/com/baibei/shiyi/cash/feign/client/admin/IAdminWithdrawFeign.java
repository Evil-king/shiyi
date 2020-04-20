package com.baibei.shiyi.cash.feign.client.admin;

import com.baibei.shiyi.cash.feign.base.dto.*;
import com.baibei.shiyi.cash.feign.base.vo.Apply1010PagelistVo;
import com.baibei.shiyi.cash.feign.base.vo.WithDrawDepositDiffVo;
import com.baibei.shiyi.cash.feign.base.vo.WithdrawListVo;
import com.baibei.shiyi.cash.feign.client.hystrix.AdminWithdrawHystrix;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@FeignClient(value = "${shiyi-cash:shiyi-cash}", path = "/admin/withdraw", fallbackFactory = AdminWithdrawHystrix.class)
public interface IAdminWithdrawFeign {

    /**
     * 出金审核
     * @param auditOrderDto
     * @return
     */
    @PostMapping("/auditWithdraw")
    ApiResult auditWithdraw(@RequestBody @Validated AuditOrderDto auditOrderDto);
    /**
     * 出金订单列表
     * @param withdrawListDto
     * @return
     */
    @PostMapping("/pagelist")
    ApiResult<MyPageInfo<WithdrawListVo>> pagelist(@RequestBody @Validated WithdrawListDto withdrawListDto);

    /**
     * 获取出金信息
     * @param withdrawListDto
     * @return
     */
    @PostMapping("/getWithdrawList")
    ApiResult<List<WithdrawListVo>> getWithdrawList(@RequestBody @Validated WithdrawListDto withdrawListDto);

    /**
     * 台账
     * @return
     */
    @PostMapping("/apply1010")
    ApiResult apply1010();


    /**
     * 台账
      * @param apply1010Dto
     * @return
     */
    @PostMapping("/apply10102")
    ApiResult apply10102(@Validated @RequestBody Apply1010Dto apply1010Dto);

    /**
     * 台账分页列表
     * @param apply1010PagelistDto
     * @return
     */
    @PostMapping("/apply1010Pagelist")
    ApiResult<MyPageInfo<Apply1010PagelistVo>> apply1010Pagelist(Apply1010PagelistDto apply1010PagelistDto);

    @PostMapping("/apply1010list")
    ApiResult<List<Apply1010PagelistVo>> apply1010List(Apply1010PagelistDto apply1010PagelistDto);

    @RequestMapping(value = "/withDrawDepositDiff/pageList", method = RequestMethod.POST)
    @ResponseBody
    ApiResult<MyPageInfo<WithDrawDepositDiffVo>> withDrawDepositDiffPageList(@RequestBody WithDrawDepositDiffDto withDrawDepositDiffDto);


    @RequestMapping(value = "/withDrawDepositDiff/withDrawDepositDiffExcelExport", method = RequestMethod.POST)
    @ResponseBody
    ApiResult<List<WithDrawDepositDiffVo>> withDrawDepositDiffExcelExport(@RequestBody WithDrawDepositDiffDto withDrawDepositDiffDto);
}
