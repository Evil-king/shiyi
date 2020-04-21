package com.baibei.shiyi.product.feign.client.hystrix;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.product.feign.bean.dto.*;
import com.baibei.shiyi.product.feign.bean.vo.BaseShelfVo;
import com.baibei.shiyi.product.feign.bean.vo.CategoryVo;
import com.baibei.shiyi.product.feign.client.admin.IAdmCategoryFeign;
import com.baibei.shiyi.product.feign.client.shiyi.GroupFeign;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author Longer
 * @date 2019/09/11
 */
@Component
@Slf4j
public class ShiyiGroupHystrix implements FallbackFactory<GroupFeign> {

    @Override
    public GroupFeign create(Throwable throwable) {
        return new GroupFeign() {

            @Override
            public ApiResult<Integer> sumGroupProduct(SumGroupProductDto sumGroupProductDto) {
                log.info("sumGroupProduct fallback，reason was：{}", throwable);
                return ApiResult.serviceFail();
            }
        };
    }
}
