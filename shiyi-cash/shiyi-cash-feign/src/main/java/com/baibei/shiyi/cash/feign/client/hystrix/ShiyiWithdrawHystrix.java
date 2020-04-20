package com.baibei.shiyi.cash.feign.client.hystrix;

import com.baibei.shiyi.cash.feign.base.dto.WithdrawForBank1312Dto;
import com.baibei.shiyi.cash.feign.base.vo.WithdrawForBank1312Vo;
import com.baibei.shiyi.cash.feign.client.shiyi.IShiyiWithdrawFeign;
import com.baibei.shiyi.common.tool.api.ApiResult;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Longer
 * @date 2019/09/11
 */
@Component
@Slf4j
public class ShiyiWithdrawHystrix implements FallbackFactory<IShiyiWithdrawFeign> {

    @Override
    public IShiyiWithdrawFeign create(Throwable throwable) {
        return new IShiyiWithdrawFeign() {

            @Override
            public ApiResult<WithdrawForBank1312Vo> withdrawForBank1312(WithdrawForBank1312Dto withdrawForBank1312Dto) {
                log.info("withdrawForBank1312 fallback，reason was：{}", throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult applySchedule1325() {
                log.info("applySchedule1325 fallback，reason was：{}", throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult applySchedule1318() {
                log.info("applySchedule1318 fallback，reason was：{}", throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult applySchedule1317() {
                log.info("applySchedule1317 fallback，reason was：{}", throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult operatorReview() {
                log.info("operatorReview fallback，reason was：{}", throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult applyScheduleFuqing315002() {
                log.info("applyScheduleFuqing fallback，reason was：{}", throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult applyScheduleFuqing315003() {
                log.info("applyScheduleFuqing315003 fallback，reason was：{}", throwable);
                return ApiResult.serviceFail();
            }
        };
    }
}
