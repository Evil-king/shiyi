package com.baibei.shiyi.account.feign.client.hystrix;

import com.baibei.shiyi.account.feign.bean.dto.OperationDto;
import com.baibei.shiyi.account.feign.bean.dto.PassCardExtractOrderListDto;
import com.baibei.shiyi.account.feign.bean.vo.PassCardExtractOrderListVo;
import com.baibei.shiyi.account.feign.client.admin.AdminPassCardExtractOrderFeign;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: hyc
 * @date: 2019/11/12 15:22
 * @description:
 */
@Component
@Slf4j
public class AdminPassCardExtractOrderHystrix implements FallbackFactory<AdminPassCardExtractOrderFeign> {
    @Override
    public AdminPassCardExtractOrderFeign create(Throwable cause) {
        return new AdminPassCardExtractOrderFeign() {
            @Override
            public ApiResult<MyPageInfo<PassCardExtractOrderListVo>> getPageList(PassCardExtractOrderListDto passCardExtractOrderListDto) {
                log.info("getPageList fallback,reason was : {}",cause);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult operation(OperationDto operationDto) {
                log.info("operation fallback,reason was : {}",cause);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<List<PassCardExtractOrderListVo>> export(PassCardExtractOrderListDto passCardExtractOrderListDto) {
                log.info("export fallback,reason was : {}",cause);
                return ApiResult.serviceFail();
            }
        };
    }
}
