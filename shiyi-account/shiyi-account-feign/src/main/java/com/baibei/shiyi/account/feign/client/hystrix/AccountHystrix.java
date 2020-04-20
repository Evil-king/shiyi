package com.baibei.shiyi.account.feign.client.hystrix;

import com.baibei.shiyi.account.feign.bean.dto.*;
import com.baibei.shiyi.account.feign.bean.vo.AccountVo;
import com.baibei.shiyi.account.feign.bean.vo.SumWithdrawAndDepositVo;
import com.baibei.shiyi.account.feign.client.AccountFeign;
import com.baibei.shiyi.common.tool.api.ApiResult;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author: hyc
 * @date: 2019/5/24 1:52 PM
 * @description:
 */
@Component
@Slf4j
public class AccountHystrix implements FallbackFactory<AccountFeign> {

    @Override
    public AccountFeign create(Throwable cause) {
        return new AccountFeign() {
            @Override
            public ApiResult<String> register(CustomerNoDto customerNoDto) {
                log.info("register fallback,reason was : {}",cause);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<String> registers(List<String> customerNos) {
                log.info("registers fallback,reason was : {}",cause);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<AccountVo> findAccount(CustomerNoDto customerNoDto) {
                log.info("findAccount fallback,reason was : {}",cause);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<String> frozenAmount(ChangeAmountDto changeAmountDto) {
                log.info("frozenAmount fallback,reason was : {}",cause);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult changeAmount(ChangeAmountDto changeAmountDto) {
                log.info("changeAmount fallback,reason was : {}",cause);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult changeMoneyAndBean(ChangeMoneyAndBeanDto changeMoneyAndBeanDto) {
                log.info("changeMoneyAndBean fallback,reason was : {}",cause);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult changeMoneyList(List<ChangeAmountDto> changeAmountDtos) {
                log.info("changeMoneyList fallback,reason was : {}",cause);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult checkByFundType(CheckByFundTypes checkByFundTypes) {
                log.info("checkByFundType fallback,reason was : {}",cause);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult checkFundPassword(String customerNo, String password) {
                log.info("checkFundPassword fallback,reason was : {}",cause);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult changeMultipleFund(List<ChangeMultipleFundDto> changeMultipleFundDto) {
                log.info("changeMultipleFund fallback,reason was : {}",cause);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<List<SumWithdrawAndDepositVo>> sumWithdrawAndDeposit(SumWithdrawAndDepositDto sumWithdrawAndDepositDto) {
                log.info("sumWithdrawAndDeposit fallback,reason was : {}",cause);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult resetWithdrawByCustomer(String customerNo, BigDecimal withdraw) {
                log.info("resetWithdrawByCustomer fallback,reason was : {}",cause);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult synchronizationBalance() {
                log.info("synchronizationBalance fallback,reason was : {}",cause);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult dealOrder(List<ChangeAmountDto> changeAmountDtos) {
                log.info("dealOrder fallback,reason was : {}",cause);
                return ApiResult.serviceFail();
            }
        };
    }
}
