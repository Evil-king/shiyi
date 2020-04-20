package com.baibei.shiyi.cash.feign.client.hystrix;

import com.baibei.shiyi.cash.feign.base.dto.*;
import com.baibei.shiyi.cash.feign.base.vo.BankOrderVo;
import com.baibei.shiyi.cash.feign.base.vo.WithdrawListVo;
import com.baibei.shiyi.cash.feign.client.admin.IAdminDealDiffFeign;
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
public class DealDiffHystrix implements FallbackFactory<IAdminDealDiffFeign> {

    @Override
    public IAdminDealDiffFeign create(Throwable throwable) {
        return new IAdminDealDiffFeign() {

            @Override
            public ApiResult diffList(String batchNo) {
                log.info("diffList fallback，reason was：{}", throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult dealDiff(DealDiffDto dealDiffDto) {
                log.info("dealDiff fallback，reason was：{}", throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<MyPageInfo<BankOrderVo>> bankOrderPageList(BankOrderDto bankOrderDto) {
                log.info("bankOrderPageList fallback，reason was：{}", throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<List<BankOrderVo>> bankOrderExcelExport(BankOrderDto bankOrderDto) {
                log.info("bankOrderExcelExport fallback，reason was：{}", throwable);
                return ApiResult.serviceFail();
            }
        };
    }
}
