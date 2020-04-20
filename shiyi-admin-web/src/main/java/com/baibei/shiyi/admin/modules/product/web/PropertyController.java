package com.baibei.shiyi.admin.modules.product.web;

import com.baibei.shiyi.common.core.aop.NoRepeatSubmit;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.product.feign.bean.dto.*;
import com.baibei.shiyi.product.feign.bean.vo.PropertyKeyVo;
import com.baibei.shiyi.product.feign.client.admin.AdminPropertyFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: hyc
 * @date: 2019/9/9 10:41
 * @description:
 */
@RestController
@RequestMapping("/admin/product/property")
public class PropertyController {
    @Autowired
    private AdminPropertyFeign propertyFeign;
    /**
     * 属性列表
     * @param propertyKeyDto
     * @return
     */
    @PostMapping("/list")
    @PreAuthorize("hasAnyRole(@authorityExpression.find('COMMON_PROP'))")
    public ApiResult<MyPageInfo<PropertyKeyVo>> propertyPageList(@Validated @RequestBody PropertyKeyDto propertyKeyDto){
        return propertyFeign.propertyPageList(propertyKeyDto);
    }
    /**
     * 添加属性
     * @param addPropertyuDto
     * @return
     */
    @PostMapping("/addProperty")
    @NoRepeatSubmit
    @PreAuthorize("hasAnyRole(@authorityExpression.add('COMMON_PROP'))")
    public ApiResult addProperty(@Validated @RequestBody AddPropertyuDto addPropertyuDto){
        return propertyFeign.addProperty(addPropertyuDto);
    }
    /**
     * 编辑属性
     * @param updatePropertyDto
     * @return
     */
    @PostMapping("/updateProperty")
    @NoRepeatSubmit
    @PreAuthorize("hasAnyRole(@authorityExpression.edit('COMMON_PROP'))")
    public ApiResult updateProperty(@Validated @RequestBody UpdatePropertyDto updatePropertyDto){
        return propertyFeign.updateProperty(updatePropertyDto);
    }
    /**
     * 删除属性
     * @param deleteIdsDto
     * @return
     */
    @PostMapping("/deleteProperty")
    @NoRepeatSubmit
    @PreAuthorize("hasAnyRole(@authorityExpression.delete('COMMON_PROP'))")
    public ApiResult deleteProperty(@Validated @RequestBody DeleteIdsDto deleteIdsDto){
        return propertyFeign.deleteProperty(deleteIdsDto);
    }

    /**
     * 根据属性id获取属性值
     * @param PropertyIdDto
     * @return
     */
    @PostMapping(path = "/getValueByKeyId")
    @ResponseBody
    ApiResult<List<PropertyValueVo>> getValueByKeyId(@Validated @RequestBody PropertyIdDto PropertyIdDto){
        return propertyFeign.getValueByKeyId(PropertyIdDto);
    }
    /**
     * 属性列表
     * @param proTypeIdDto
     * @return
     */
    @PostMapping("/getPropertyList")
    public ApiResult<List<PropertyKeyVo>> getPropertyList(@Validated @RequestBody ProTypeIdDto proTypeIdDto){
        return propertyFeign.getPropertyList(proTypeIdDto);
    }
}
