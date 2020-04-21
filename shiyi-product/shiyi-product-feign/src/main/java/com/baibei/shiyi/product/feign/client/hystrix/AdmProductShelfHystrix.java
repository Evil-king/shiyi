package com.baibei.shiyi.product.feign.client.hystrix;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.product.feign.bean.dto.*;
import com.baibei.shiyi.product.feign.bean.vo.*;
import com.baibei.shiyi.product.feign.client.admin.IAdmProductFeign;
import com.baibei.shiyi.product.feign.client.admin.IAdmProductShelfFeign;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Longer
 * @date 2019/08/1
 */
@Component
@Slf4j
public class AdmProductShelfHystrix implements FallbackFactory<IAdmProductShelfFeign> {

    @Override
    public IAdmProductShelfFeign create(Throwable throwable) {
        return new IAdmProductShelfFeign() {

            @Override
            public ApiResult<MyPageInfo<BaseShelfVo>> pageList(AdmSelectProductShelfDto admSelectProductShelfDto) {
                log.info("shelfPageList product current exception is {}", throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult shelfProduct(AdmProductShelfDto admProductShelfDto) {
                log.info("shelfProduct product current exception is {}", throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<AdmEditProductShelfVo> editShelfProductPage(AdmEditProductShelfPageDto admEditProductShelfPageDto) {
                log.info("editShelfProductPage product current exception is {}", throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<AdmToAddProductShelfVo> toAddShelfProductPage() {
                log.info("toAddShelfProductPage product current exception is {}", throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<List<AdmProductShelfSkuVo>> getAdmShelfSkuInfoList(AdmEditProductShelfPageDto admEditProductShelfPageDto) {
                log.info("getAdmShelfSkuInfoList product current exception is {}", throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult deleteProductShelf(AdmEditProductShelfPageDto admEditProductShelfPageDto) {
                log.info("deleteProductShelf product current exception is {}", throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult batchDeleletProductShelf(List<BatchDeleteShelfDto> batchDeleteShelfDtoList) {
                log.info("batchDeleletProductShelf product current exception is {}", throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult shelf(ShelfProductDto shelfProductDto) {
                log.info("shelf product current exception is {}", throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult batchShelf(List<ShelfProductDto> shelfProductDtoList) {
                log.info("batchShelf product current exception is {}", throwable);
                return ApiResult.serviceFail();
            }
        };
    }
}
