package com.baibei.shiyi.product.feign.client.hystrix;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.product.feign.bean.dto.*;
import com.baibei.shiyi.product.feign.bean.vo.PropertyKeyVo;
import com.baibei.shiyi.product.feign.client.admin.AdminPropertyFeign;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class AdminPropertyHystrix implements FallbackFactory<AdminPropertyFeign> {


    @Override
    public AdminPropertyFeign create(Throwable cause) {
        return new AdminPropertyFeign() {

            @Override
            public ApiResult<MyPageInfo<PropertyKeyVo>> propertyPageList(PropertyKeyDto propertyKeyDto) {
                log.info("propertyPageList fallback，reason was：{}", cause);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult addProperty(AddPropertyuDto addPropertyuDto) {
                log.info("addProperty fallback，reason was：{}", cause);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult updateProperty(UpdatePropertyDto updatePropertyDto) {
                log.info("updateProperty fallback，reason was：{}", cause);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult deleteProperty(DeleteIdsDto deleteIdsDto) {
                log.info("deleteProperty fallback，reason was：{}", cause);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<List<PropertyValueVo>> getValueByKeyId(PropertyIdDto PropertyIdDto) {
                log.info("getValueByKeyId fallback，reason was：{}", cause);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<List<PropertyKeyVo>> getPropertyList(ProTypeIdDto proTypeIdDto) {
                log.info("getPropertyList fallback，reason was：{}", cause);
                return ApiResult.serviceFail();
            }
        };
    }
}
