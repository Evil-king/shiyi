package com.baibei.shiyi.account.feign.client.hystrix;

import com.baibei.shiyi.account.feign.bean.dto.*;
import com.baibei.shiyi.account.feign.bean.vo.AccountVo;
import com.baibei.shiyi.account.feign.bean.vo.SumWithdrawAndDepositVo;
import com.baibei.shiyi.account.feign.client.AccountFeign;
import com.baibei.shiyi.account.feign.client.PassCardExtractOrderFeign;
import com.baibei.shiyi.common.tool.api.ApiResult;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author: hyc
 * @date: 2019/5/24 1:52 PM
 * @description:
 */
@Component
@Slf4j
public class PassCardExtractOrderHystrix implements FallbackFactory<PassCardExtractOrderFeign> {


    @Override
    public PassCardExtractOrderFeign create(Throwable cause) {
        return new PassCardExtractOrderFeign() {
            @Override
            public ApiResult systemOperation() {
                log.info("systemOperation fallback,reason was : {}",cause);
                return ApiResult.serviceFail();
            }
        };
    }
}
