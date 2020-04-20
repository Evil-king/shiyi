package com.baibei.shiyi.cash.feign.client.hystrix;

import com.baibei.shiyi.cash.feign.base.dto.*;
import com.baibei.shiyi.cash.feign.base.vo.Apply1010PagelistVo;
import com.baibei.shiyi.cash.feign.base.vo.WithDrawDepositDiffVo;
import com.baibei.shiyi.cash.feign.base.vo.WithdrawListVo;
import com.baibei.shiyi.cash.feign.client.admin.IAdminWithdrawFeign;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Longer
 * @date 2019/09/11
 */
@Component
@Slf4j
public class AdminWithdrawHystrix implements FallbackFactory<IAdminWithdrawFeign> {

    @Override
    public IAdminWithdrawFeign create(Throwable throwable) {
        return new IAdminWithdrawFeign() {

            @Override
            public ApiResult auditWithdraw(AuditOrderDto auditOrderDto) {
                log.info("auditWithdraw fallback，reason was：{}", throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<MyPageInfo<WithdrawListVo>> pagelist(WithdrawListDto withdrawListDto) {
                log.info("withdrawPagelist fallback，reason was：{}", throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<List<WithdrawListVo>> getWithdrawList(WithdrawListDto withdrawListDto) {
                log.info("getWithdrawList fallback，reason was：{}", throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult apply1010() {
                log.info("apply1010 fallback，reason was：{}", throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult apply10102(Apply1010Dto apply1010Dto) {
                log.info("apply10102 fallback，reason was：{}", throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<MyPageInfo<Apply1010PagelistVo>> apply1010Pagelist(Apply1010PagelistDto apply1010PagelistDto) {
                log.info("Apply1010PagelistVo fallback，reason was：{}", throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<List<Apply1010PagelistVo>> apply1010List(Apply1010PagelistDto apply1010PagelistDto) {
                log.info("apply1010List fallback，reason was：{}", throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<MyPageInfo<WithDrawDepositDiffVo>> withDrawDepositDiffPageList(WithDrawDepositDiffDto withDrawDepositDiffDto) {
                log.info("withDrawDepositDiffPageList fallback，reason was：{}", throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<List<WithDrawDepositDiffVo>> withDrawDepositDiffExcelExport(WithDrawDepositDiffDto withDrawDepositDiffDto) {
                log.info("withDrawDepositDiffExcelExport fallback，reason was：{}", throwable);
                return ApiResult.serviceFail();
            }
        };
    }
}
