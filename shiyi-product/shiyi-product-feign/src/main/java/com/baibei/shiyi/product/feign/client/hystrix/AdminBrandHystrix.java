package com.baibei.shiyi.product.feign.client.hystrix;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.product.feign.bean.dto.AddOrUpdateBrandDto;
import com.baibei.shiyi.product.feign.bean.dto.BrandListDto;
import com.baibei.shiyi.product.feign.bean.dto.DeleteIdsDto;
import com.baibei.shiyi.product.feign.bean.vo.BrandListVo;
import com.baibei.shiyi.product.feign.client.admin.AdminBrandFeign;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: hyc
 * @date: 2019/9/12 10:03
 * @description:
 */
@Component
@Slf4j
public class AdminBrandHystrix implements FallbackFactory<AdminBrandFeign> {
    @Override
    public AdminBrandFeign create(Throwable cause) {
        return new AdminBrandFeign() {
            @Override
            public ApiResult<MyPageInfo<BrandListVo>> brandList(BrandListDto brandListDto) {
                log.info("brandList current exception is {}", cause);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult addOrUpdateBrand(AddOrUpdateBrandDto addOrUpdateBrandDto) {
                log.info("addOrUpdateBrand current exception is {}", cause);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult deleteBrand(DeleteIdsDto deleteIdsDto) {
                log.info("deleteBrand current exception is {}", cause);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<List<BrandListVo>> getBrandList() {
                log.info("getBrandList current exception is {}", cause);
                return ApiResult.serviceFail();
            }
        };
    }
}
