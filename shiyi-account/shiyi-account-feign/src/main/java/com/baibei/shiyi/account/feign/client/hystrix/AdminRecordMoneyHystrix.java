package com.baibei.shiyi.account.feign.client.hystrix;

import com.baibei.shiyi.account.feign.bean.dto.AdminRecordDto;
import com.baibei.shiyi.account.feign.bean.vo.AdminRecordVo;
import com.baibei.shiyi.account.feign.client.AdminRecordMoneyFeign;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: hyc
 * @date: 2019/11/1 18:00
 * @description:
 */
@Component
@Slf4j
public class AdminRecordMoneyHystrix implements FallbackFactory<AdminRecordMoneyFeign> {

    @Override
    public AdminRecordMoneyFeign create(Throwable cause) {
        return new AdminRecordMoneyFeign() {
            @Override
            public ApiResult<MyPageInfo<AdminRecordVo>> pageList(AdminRecordDto recordDto) {
                log.info("pageList fallback,reason was : {}",cause);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<List<AdminRecordVo>> list(AdminRecordDto recordDto) {
                log.info("list fallback,reason was : {}",cause);
                return ApiResult.serviceFail();
            }
        };
    }
}
