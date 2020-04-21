package com.baibei.shiyi.product.feign.client.hystrix;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.product.feign.bean.dto.*;
import com.baibei.shiyi.product.feign.bean.vo.ParameterListVo;
import com.baibei.shiyi.product.feign.client.admin.AdminParameterFeign;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: hyc
 * @date: 2019/9/11 10:16
 * @description:
 */
@Component
@Slf4j
public class AdminParameterHystrix implements FallbackFactory<AdminParameterFeign> {
    @Override
    public AdminParameterFeign create(Throwable cause) {
        return new AdminParameterFeign() {

            @Override
            public ApiResult<MyPageInfo<ParameterListVo>> parameterPageList(ParameterListDto parameterListDto) {
                log.info("parameterPageList fallback，reason was：{}", cause);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult addParameter(AddParameterDto addParameterDto) {
                log.info("addParameter fallback，reason was：{}", cause);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult updateParameter(UpdateParameterDto updateParameterDto) {
                log.info("updateParameter fallback，reason was：{}", cause);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult deleteParameter(DeleteIdsDto deleteIdsDto) {
                log.info("deleteParameter fallback，reason was：{}", cause);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<List<ParameterListVo>> getParameterList(ProTypeIdDto proTypeIdDto) {
                log.info("getParameterList fallback，reason was：{}", cause);
                return ApiResult.serviceFail();
            }
        };
    }
}
