package com.baibei.shiyi.account.web.shiyi;

import com.baibei.shiyi.account.feign.bean.dto.*;
import com.baibei.shiyi.account.feign.bean.vo.AccountVo;
import com.baibei.shiyi.account.feign.bean.vo.SumWithdrawAndDepositVo;
import com.baibei.shiyi.account.model.Account;
import com.baibei.shiyi.account.service.IAccountService;
import com.baibei.shiyi.account.service.ICustomerBeanService;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.account.feign.base.shiyi.IAccountBase;
import com.baibei.shiyi.common.tool.api.ResultEnum;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.exception.ServiceException;
import com.baibei.shiyi.common.tool.utils.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author: hyc
 * @date: 2019/5/24 14:51
 * @description:
 */
@Controller
@Slf4j
public class ShiyiAccountController implements IAccountBase {
    @Autowired
    private IAccountService accountService;

    @Autowired
    private ICustomerBeanService customerBeanService;

    /**
     * 注册
     * @param customerNoDto
     * @return
     */
    @Override
    public ApiResult<String> register(@RequestBody  @Validated CustomerNoDto customerNoDto) {
        return accountService.register(customerNoDto);
    }

    @Override
    public ApiResult<String> registers(@RequestBody  List<String> customerNos) {
        for (int i = 0; i <customerNos.size() ; i++) {
            CustomerNoDto customerNoDto=new CustomerNoDto();
            customerNoDto.setCustomerNo(customerNos.get(i));
            accountService.register(customerNoDto);
        }
        return ApiResult.success();
    }

    /**
     * 查询账户余额等信息
     * @param customerNoDto
     * @return
     */
    @Override
    public ApiResult<AccountVo> findAccount(@RequestBody  @Validated CustomerNoDto customerNoDto) {
        return accountService.findAccount(customerNoDto);
    }

    /**
     * 请求 冻结/解冻 资金
     * @param changeAmountDto
     * @return
     */
    @Override
    public ApiResult<String> frozenAmount(@RequestBody  @Validated ChangeAmountDto changeAmountDto) {
        Account account=accountService.checkAccount(changeAmountDto.getCustomerNo());
        if(account==null){
            //没找到用户
            ApiResult result=new ApiResult();
            result.setCode(ResultEnum.ACCOUNT_NOT_EXIST.getCode());
            result.setMsg(ResultEnum.ACCOUNT_NOT_EXIST.getMsg());
            return result;
        }
        if(Constants.Retype.OUT.equals(changeAmountDto.getReType())){
            //冻结类型为冻结
            return accountService.frozenAmount(changeAmountDto,account);
        }else if(Constants.Retype.IN.equals(changeAmountDto.getReType())){
            //冻结类型为解冻
            return accountService.thawAmount(changeAmountDto,account);
        }else{
            return ApiResult.badParam("冻结类型不明确");
        }
    }

    @Override
    public ApiResult changeAmount(@RequestBody @Validated ChangeAmountDto changeAmountDto) {
        return accountService.changeAmount(changeAmountDto);
    }

    @Override
    public ApiResult changeMoneyAndBean(@RequestBody @Validated ChangeMoneyAndBeanDto changeMoneyAndBeanDto) {
        return accountService.changeMoneyAndBean(changeMoneyAndBeanDto);
    }

    @Override
    public ApiResult changeMoneyList(@RequestBody @Validated List<ChangeAmountDto> changeAmountDtos) {

        return accountService.changeMoneyList(changeAmountDtos);
    }

    @Override
    public ApiResult checkByFundType(@RequestBody @Validated CheckByFundTypes checkByFundTypes) {
        return accountService.checkByFundType(checkByFundTypes);

    }

    @Override
    public ApiResult checkFundPassword(@RequestParam("customerNo") String customerNo, @RequestParam("password") String password) {
        return accountService.checkFundPassword(customerNo,password);
    }

    @Override
    public ApiResult changeMultipleFund(@RequestBody @Validated List<ChangeMultipleFundDto> changeMultipleFundDtos) {
        return accountService.changeMultipleFund(changeMultipleFundDtos);
    }

    @Override
    public ApiResult<List<SumWithdrawAndDepositVo>> sumWithdrawAndDeposit(@RequestBody @Validated SumWithdrawAndDepositDto sumWithdrawAndDepositDto) {
        List<SumWithdrawAndDepositVo> sumWithdrawAndDepositVoList=accountService.sumWithdrawAndDeposit(sumWithdrawAndDepositDto);
        return ApiResult.success(sumWithdrawAndDepositVoList);
    }

    @Override
    public ApiResult synchronizationBalance() {
        return accountService.synchronizationBalance();
    }

    /**
     * 摘牌卖出
     * @param changeAmountDtos
     * @return
     */
    @Override
    public ApiResult dealOrder(@RequestBody @Validated List<ChangeAmountDto> changeAmountDtos) {
        if(changeAmountDtos.size()!=2){
            return ApiResult.badParam();
        }

        return accountService.dealOrder(changeAmountDtos);
    }

    @Override
    public ApiResult resetWithdrawByCustomer(String customerNo, BigDecimal withdraw) {
        return accountService.resetWithdrawByCustomer(customerNo,withdraw);
    }
}
