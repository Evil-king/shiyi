package com.baibei.shiyi.account.service;

import com.baibei.shiyi.account.common.dto.ConsignmentDto;
import com.baibei.shiyi.account.common.dto.InsertFundPasswordDto;
import com.baibei.shiyi.account.common.vo.AccountDetailVo;
import com.baibei.shiyi.account.feign.bean.dto.*;
import com.baibei.shiyi.account.feign.bean.vo.AccountVo;
import com.baibei.shiyi.account.feign.bean.vo.FundDetailVo;
import com.baibei.shiyi.account.feign.bean.vo.FundInformationVo;
import com.baibei.shiyi.account.feign.bean.vo.SumWithdrawAndDepositVo;
import com.baibei.shiyi.account.model.Account;
import com.baibei.shiyi.cash.feign.base.dto.OrderWithdrawDto;
import com.baibei.shiyi.cash.feign.base.message.DealDiffMessage;
import com.baibei.shiyi.common.core.mybatis.Service;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.user.feign.bean.dto.UpdatePasswordDto;

import java.math.BigDecimal;
import java.util.List;


/**
* @author: hyc
* @date: 2019/05/24 10:40:51
* @description: Account服务接口
*/
public interface IAccountService extends Service<Account> {
    /**
     * 注册
     * @param customerNoDto
     * @return
     */
    ApiResult<String> register(CustomerNoDto customerNoDto);

    /**
     * 查找账户信息
     * @param customerNoDto
     * @return
     */
    ApiResult<AccountVo> findAccount(CustomerNoDto customerNoDto);

    /**
     * 请求 冻结/解冻 资金
     * @param changeAmountDto
     * @param account
     * @return
     */
    ApiResult<String> frozenAmount(ChangeAmountDto changeAmountDto, Account account);

    /**
     * 成交解冻扣除金额
     * @param frozenAmountDto
     * @return
     */
    ApiResult thawAmountByTrade(ChangeAmountDto frozenAmountDto) ;
    /**
     * 修改资金
     * @return
     */
    ApiResult changeAmount(ChangeAmountDto changeAmountDto);

    Account checkAccount(String customerNo);

    /**
     * 解冻资金
     * @param changeAmountDto
     * @param data
     * @return
     */
    ApiResult<String> thawAmount(ChangeAmountDto changeAmountDto, Account data);

    /**
     * 修改资金密码
     * @param updatePasswordDto
     * @return
     */
    ApiResult<String> updatePassword(UpdatePasswordDto updatePasswordDto);

    ApiResult<FundInformationVo> fundInformation(CustomerNoDto customerNoDto);

    ApiResult<FundDetailVo> fundDetail(CustomerNoDto customerNoDto);

    ApiResult<String> findCustomerNoByMobile(String mobile);

    boolean checkMobileVerificationCode(String mobile, String mobileVerificationCode,String mobileVerificationType);

    ApiResult<String> forgetPassword(String customerNo, String password);

    ApiResult insertFundPassword(InsertFundPasswordDto fundPasswordDto);

    /**
     * 修改资金以及积分
     * @param changeMoneyAndBeanDto
     * @return
     */
    ApiResult changeMoneyAndBean(ChangeMoneyAndBeanDto changeMoneyAndBeanDto);

    /**
     * 判断资金密码是否正确
     * @param customerNo
     * @param password
     * @return
     */
    ApiResult checkFundPassword(String customerNo, String password);

    /**
     * 申请寄售
     * @param consignmentDto
     * @return
     */
    ApiResult consignmentApplication(ConsignmentDto consignmentDto);

    ApiResult changeMultipleFund(List<ChangeMultipleFundDto> changeMultipleFundDtos);

    ApiResult checkByFundType(CheckByFundTypes checkByFundTypes);

    /**
     * 出金扣钱
     * @param orderWithdrawDto
     */
    void withdrawDetuchMoney(OrderWithdrawDto orderWithdrawDto);

    ApiResult updateWithDraw(ChangeAmountDto changeAmountDto);

    /**
     * 出入金调账
     * @param dealDiffMessage
     */
    void dealDiffChangeAmount(DealDiffMessage dealDiffMessage);

    /**
     * 获取某个时间段内的每个用户的总出金和总入金
     * @param sumWithdrawAndDepositDto
     * @return
     */
    List<SumWithdrawAndDepositVo> sumWithdrawAndDeposit(SumWithdrawAndDepositDto sumWithdrawAndDepositDto);

    /**
     * 重置某个用户的可提余额
     * @return
     * @param customerNo
     * @param withdraw
     */
    ApiResult resetWithdrawByCustomer(String customerNo, BigDecimal withdraw);

    /**
     * 同步所有用户的可提金额
     * @return
     */
    ApiResult synchronizationBalance();

    /**
     * 摘牌卖出，同时操作2个用户的资金
     * @param changeAmountDtos
     * @return
     */
    ApiResult dealOrder(List<ChangeAmountDto> changeAmountDtos);

    ApiResult<AccountDetailVo> getAccountDetail(CustomerNoDto customerNoDto);

    /**
     * 修改多个用户的资金
     * @param changeAmountDtos
     * @return
     */
    ApiResult changeMoneyList(List<ChangeAmountDto> changeAmountDtos);
}
