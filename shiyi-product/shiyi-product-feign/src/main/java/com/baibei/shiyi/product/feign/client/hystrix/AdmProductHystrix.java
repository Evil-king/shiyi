package com.baibei.shiyi.product.feign.client.hystrix;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.product.feign.bean.dto.*;
import com.baibei.shiyi.product.feign.bean.vo.*;
import com.baibei.shiyi.product.feign.client.admin.IAdmProductFeign;
import com.baibei.shiyi.product.feign.client.shiyi.ProductFeign;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Longer
 * @date 2019/08/1
 */
@Component
@Slf4j
public class AdmProductHystrix implements FallbackFactory<IAdmProductFeign> {

    @Override
    public IAdmProductFeign create(Throwable throwable) {
        return new IAdmProductFeign() {
            @Override
            public ApiResult<MyPageInfo<AdmProductVo>> pageList(AdmProductDto admProductDto) {
                log.info("product pageList current exception is {}", throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult addOrEditProduct(AddProductDto addProductDto) {
                log.info("add product current exception is {}", throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<AdmEditProductVo> editProductPage(AdmEditProductPageDto admEditProductPageDto) {
                log.info("editProductPage product current exception is {}", throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult deleteProduct(AdmDeleteProductDto AdmDeleteProductDto) {
                log.info("deleteProduct product current exception is {}", throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult batchDeleteProduct(List<AdmDeleteProductDto> AdmDeleteProductDto) {
                log.info("batchDeleteProduct product current exception is {}", throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<List<BaseProductSkuVo>> getProductSkuList(SpuDto spuDto) {
                log.info("getProductSkuList product current exception is {}", throwable);
                return ApiResult.serviceFail();
            }
        };
    }
}
