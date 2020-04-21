package com.baibei.shiyi.product.feign.client.hystrix;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.common.tool.page.PageParam;
import com.baibei.shiyi.product.feign.bean.dto.AddProTypeDto;
import com.baibei.shiyi.product.feign.bean.dto.DeleteIdsDto;
import com.baibei.shiyi.product.feign.bean.dto.UpdateProTypeDto;
import com.baibei.shiyi.product.feign.bean.vo.ProTypeVo;
import com.baibei.shiyi.product.feign.client.admin.AdminProductProTypeFeign;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: hyc
 * @date: 2019/9/6 16:23
 * @description:
 */
@Component
@Slf4j
public class AdminProductProTypeHystrix implements FallbackFactory<AdminProductProTypeFeign> {
    @Override
    public AdminProductProTypeFeign create(Throwable cause) {
        return new AdminProductProTypeFeign(){

            @Override
            public ApiResult<MyPageInfo<ProTypeVo>> list(PageParam pageParam) {
                log.info("list fallback，reason was：{}", cause);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<List<ProTypeVo>> getAll() {
                log.info("getAll fallback，reason was：{}", cause);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult add(AddProTypeDto addProTypeDto) {
                log.info("add fallback，reason was：{}", cause);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult update(UpdateProTypeDto updateProTypeDto) {
                log.info("update fallback，reason was：{}", cause);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult delete(DeleteIdsDto deleteIdsDto) {
                log.info("delete fallback，reason was：{}", cause);
                return ApiResult.serviceFail();
            }
        };
    }
}
