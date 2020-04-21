package com.baibei.shiyi.product.web.admin;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.product.feign.base.admin.IAdminPropertyBase;
import com.baibei.shiyi.product.feign.bean.dto.*;
import com.baibei.shiyi.product.feign.bean.vo.PropertyKeyVo;
import com.baibei.shiyi.product.service.IPropertyKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @Classname AdminPropertyController
 * @Description 商品规格相关
 * @Date 2019/7/30 17:46
 * @Created by Longer
 */

@RestController
public class AdminPropertyController implements IAdminPropertyBase {
    @Autowired
    private IPropertyKeyService propertyKeyService;

    @Override
    public ApiResult<MyPageInfo<PropertyKeyVo>> propertyPageList(@Validated @RequestBody PropertyKeyDto propertyKeyDto) {
        return propertyKeyService.propertyPageList(propertyKeyDto);
    }

    public ApiResult addProperty(@Validated @RequestBody AddPropertyuDto addPropertyuDto) {
        return propertyKeyService.addProperty(addPropertyuDto);
    }

    @Override
    public ApiResult updateProperty(@Validated @RequestBody UpdatePropertyDto updatePropertyDto) {
        return propertyKeyService.updateProperty(updatePropertyDto);
    }

    @Override
    public ApiResult deleteProperty(@Validated @RequestBody DeleteIdsDto deleteIdsDto) {
        return propertyKeyService.deleteProperty(deleteIdsDto);
    }

    @Override
    public ApiResult<List<PropertyValueVo>> getValueByKeyId(@Validated @RequestBody PropertyIdDto PropertyIdDto) {
        return propertyKeyService.getValueByKeyId(PropertyIdDto.getId());
    }

    @Override
    public ApiResult<List<PropertyKeyVo>> getPropertyList(@Validated @RequestBody ProTypeIdDto proTypeIdDto) {
        return propertyKeyService.getPropertyList(proTypeIdDto);
    }
}
