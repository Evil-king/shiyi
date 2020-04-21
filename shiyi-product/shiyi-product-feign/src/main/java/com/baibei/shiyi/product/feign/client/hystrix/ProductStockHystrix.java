package com.baibei.shiyi.product.feign.client.hystrix;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.product.feign.bean.dto.ChangeStockDto;
import com.baibei.shiyi.product.feign.bean.dto.GroupCurdDto;
import com.baibei.shiyi.product.feign.bean.dto.GroupDto;
import com.baibei.shiyi.product.feign.bean.dto.ProductGroupDto;
import com.baibei.shiyi.product.feign.bean.vo.GroupProductVo;
import com.baibei.shiyi.product.feign.bean.vo.GroupVo;
import com.baibei.shiyi.product.feign.client.admin.IGroupFeign;
import com.baibei.shiyi.product.feign.client.admin.IProductStockFeign;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class ProductStockHystrix implements FallbackFactory<IProductStockFeign> {

    private final Logger logger = LoggerFactory.getLogger(ProductStockHystrix.class);

    @Override
    public IProductStockFeign create(Throwable throwable) {
        return new IProductStockFeign() {

            @Override
            public ApiResult changeStock(ChangeStockDto changeStockDto) {
                log.info("changeStock fallback，reason was：{}", throwable);
                return ApiResult.serviceFail();
            }
        };
    }
}
