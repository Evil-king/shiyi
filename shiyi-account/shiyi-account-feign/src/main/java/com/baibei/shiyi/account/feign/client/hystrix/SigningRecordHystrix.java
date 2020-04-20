package com.baibei.shiyi.account.feign.client.hystrix;

import com.alibaba.fastjson.JSONObject;
import com.baibei.shiyi.account.feign.bean.dto.PABSigningRecordDto;
import com.baibei.shiyi.account.feign.bean.vo.PABSigningRecordVo;
import com.baibei.shiyi.account.feign.bean.vo.SigningRecordVo;
import com.baibei.shiyi.account.feign.client.ISigningRecordFeign;
import com.baibei.shiyi.common.tool.api.ApiResult;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class SigningRecordHystrix implements FallbackFactory<ISigningRecordFeign> {
    @Override
    public ISigningRecordFeign create(Throwable throwable) {
        return new ISigningRecordFeign() {
            @Override
            public ApiResult<SigningRecordVo> findByThirdCustId(String customerNo) {
                log.info("findByThirdCustId fallbackFactory customerNO is {}, exception is {}", customerNo, throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<PABSigningRecordVo> signingRecord(PABSigningRecordDto signingRecordDto) {
                log.info("signingRecord fallbackFactory signingRecordDto is {}, exception is {}", JSONObject.toJSONString(signingRecordDto), throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<SigningRecordVo> findByCustAcctId(String custAcctId) {
                log.info("findByCustAcctId fallbackFactory custAcctId is {},exception is {}", custAcctId, throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<Boolean> isTodaySigning(String customerNo) {
                log.info("isTodaySigning fallbackFactory customerNo is {},exception is {}", customerNo, throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<List<SigningRecordVo>> findByThirdCustIds(List<String> customerNos) {
                log.info("findByThirdCustIds fallbackFactory  throwable is {}",throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<String> getBalance(String message) {
                log.info("getBalance fallbackFactory  throwable is {}",throwable);
                return ApiResult.serviceFail();
            }
        };
    }
}
