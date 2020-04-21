package com.baibei.shiyi.product.feign.client.hystrix;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.product.feign.bean.dto.ChangeStockDto;
import com.baibei.shiyi.product.feign.bean.dto.ShelfRefDto;
import com.baibei.shiyi.product.feign.bean.vo.BaseShelfVo;
import com.baibei.shiyi.product.feign.bean.vo.ProductSkuVo;
import com.baibei.shiyi.product.feign.client.shiyi.ProductFeign;
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
public class ProductHystrix implements FallbackFactory<ProductFeign> {

    @Override
    public ProductFeign create(Throwable throwable) {
        return new ProductFeign() {
            @Override
            public ApiResult<BaseShelfVo> getBaseShelfProductInfo(ShelfRefDto shelfRefDto) {
                log.info("getBaseShelfProductInfo fallback，reason was：{}", throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<List<BaseShelfVo>> getBatchShelfProductInfo(List<ShelfRefDto> shelfRefDtoList) {
                log.info("getBatchShelfProductInfo fallback，reason was：{}", throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<ProductSkuVo> getProductSkuBySkuId(Long skuId) {
                log.info("getProductSkuBySkuId fallback，reason was：{}", throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult changeStock(ChangeStockDto changeStockDto) {
                log.info("changeStock fallback，reason was：{}", throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult batchChangeStock(List<ChangeStockDto> changeStockDtoList) {
                log.info("batchChangeStock fallback，reason was：{}", throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<BaseShelfVo> getShelfInfo(ShelfRefDto shelfRefDto) {
                log.info("getShelfInfo fallback，reason was：{}", throwable);
                return ApiResult.serviceFail();
            }
        };
    }
}
