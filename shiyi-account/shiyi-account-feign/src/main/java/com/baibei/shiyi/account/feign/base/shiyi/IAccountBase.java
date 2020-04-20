package com.baibei.shiyi.account.feign.base.shiyi;

import com.baibei.shiyi.account.feign.bean.dto.*;
import com.baibei.shiyi.account.feign.bean.vo.AccountVo;
import com.baibei.shiyi.account.feign.bean.vo.SumWithdrawAndDepositVo;
import com.baibei.shiyi.common.tool.api.ApiResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author: hyc
 * @date: 2019/5/2414:32
 * @description:
 */
public interface IAccountBase {
    /**
     * 注册
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/shiyi/account/save", method = RequestMethod.POST)
    @ResponseBody
    ApiResult<String> register(@RequestBody @Validated CustomerNoDto customerNoDto);


    @RequestMapping(value="/shiyi/account/saveList")
    @ResponseBody
    ApiResult<String> registers(@RequestBody @Validated List<String> customerNos);

    /**
     * 查询资金信息
     * @param customerNoDto
     * @return
     */
    @RequestMapping(value = "/shiyi/account/findAccount",method = RequestMethod.POST)
    @ResponseBody
    ApiResult<AccountVo> findAccount(@RequestBody @Validated CustomerNoDto customerNoDto);

    /**
     * 请求冻结/解冻 资金
     * @param changeAmountDto
     * @return
     */
    @RequestMapping(value = "/shiyi/account/frozenAmount",method = RequestMethod.POST)
    @ResponseBody
    ApiResult<String> frozenAmount(@RequestBody @Validated ChangeAmountDto changeAmountDto);
    /**
     * 修改可用资金
     */
    @RequestMapping(value = "/shiyi/account/changeAmount",method = RequestMethod.POST)
    @ResponseBody
    ApiResult changeAmount(@RequestBody @Validated ChangeAmountDto changeAmountDto);

    /**
     * 同时修改资金以及积分金额
     */
    @RequestMapping(value = "/shiyi/account/changeMoneyAndBean",method = RequestMethod.POST)
    @ResponseBody
    ApiResult changeMoneyAndBean(@RequestBody @Validated ChangeMoneyAndBeanDto changeMoneyAndBeanDto);

    /**
     * 同时修改多个用户的资金
     * @param changeAmountDtos
     * @return
     */
    @RequestMapping(value="/shiyi/account/changeMoneyList",method = RequestMethod.POST)
    @ResponseBody
    ApiResult changeMoneyList(@RequestBody @Validated  List<ChangeAmountDto> changeAmountDtos);


    /**
     * 根据用户判断多个类型的金额是否足够
     */
    @RequestMapping(value = "/shiyi/account/checkByFundType",method = RequestMethod.POST)
    @ResponseBody
    ApiResult checkByFundType(@RequestBody @Validated CheckByFundTypes checkByFundTypes);

    /**
     * 根据用户编号以及资金密码判断资金密码是否正确
     */
    @GetMapping("/shiyi/account/checkFundPassword")
    @ResponseBody
    ApiResult checkFundPassword(@RequestParam("customerNo") String customerNo,@RequestParam("password") String password);

    /**
     * 修改多个用户的多个资金类型的余额
     */
    @PostMapping("/shiyi/account/changeMultipleFund")
    @ResponseBody
    ApiResult changeMultipleFund(@RequestBody @Validated List<ChangeMultipleFundDto> changeMultipleFundDto);


    /**
     * 统计某个时间段内，每个用户的总出金和总入金
     */
    @PostMapping("/shiyi/account/sumWithdrawAndDeposit")
    @ResponseBody
    ApiResult<List<SumWithdrawAndDepositVo>> sumWithdrawAndDeposit(@RequestBody @Validated SumWithdrawAndDepositDto sumWithdrawAndDepositDto);

    /**
     * 重置某个用户的可提余额
     * @param customerNo
     * @param withdraw
     * @return
     */
    @GetMapping("/shiyi/account/resetWithdrawByCustomer")
    @ResponseBody
    ApiResult resetWithdrawByCustomer(@RequestParam("customerNo") String customerNo, @RequestParam("withdraw")BigDecimal withdraw);

    @PostMapping("/shiyi/account/synchronizationBalance")
    @ResponseBody
    ApiResult synchronizationBalance();

    /**
     * 摘牌卖出 操作2个账户，支出方为挂牌买入方，收入方为摘牌卖出方
     * @return
     */
    @PostMapping("/shiyi/account/dealOrder")
    @ResponseBody
    ApiResult dealOrder(@RequestBody @Validated  List<ChangeAmountDto> changeAmountDtos);
}
