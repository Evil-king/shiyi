package com.baibei.shiyi.product.feign.base.admin;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.common.tool.page.PageParam;
import com.baibei.shiyi.product.feign.bean.dto.*;
import com.baibei.shiyi.product.feign.bean.vo.ProTypeVo;
import com.baibei.shiyi.product.feign.bean.vo.PropertyKeyVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author: hyc
 * @date: 2019/9/9 10:29
 * @description:
 */
public interface IAdminPropertyBase {
    /**
     * 属性列表
     * @param propertyKeyDto
     * @return
     */
    @PostMapping("/admin/product/property/list")
    @ResponseBody
    ApiResult<MyPageInfo<PropertyKeyVo>> propertyPageList(@Validated @RequestBody PropertyKeyDto propertyKeyDto);

    /**
     * 添加商品属性
     * @param addPropertyuDto
     * @return
     */
    @PostMapping(path = "/admin/product/property/addProperty")
    @ResponseBody
    ApiResult addProperty(@Validated @RequestBody AddPropertyuDto addPropertyuDto);

    /**
     * 修改商品属性
     * @param updatePropertyDto
     * @return
     */
    @PostMapping(path = "/admin/product/property/updateProperty")
    @ResponseBody
    ApiResult updateProperty(@Validated @RequestBody UpdatePropertyDto updatePropertyDto);

    /**
     * 删除商品属性
     * @param deleteIdsDto
     * @return
     */
    @PostMapping(path = "/admin/product/property/deleteProperty")
    @ResponseBody
    ApiResult deleteProperty(@Validated @RequestBody DeleteIdsDto deleteIdsDto);

    /**
     * 根据属性id获取属性值
     * @param PropertyIdDto
     * @return
     */
    @PostMapping(path = "/admin/product/property/getValueByKeyId")
    @ResponseBody
    ApiResult<List<PropertyValueVo>> getValueByKeyId(@Validated @RequestBody PropertyIdDto PropertyIdDto);
    /**
     * 根据后台类目id获取属性值
     * @param proTypeIdDto
     * @return
     */
    @PostMapping(path = "/admin/product/property/getPropertyList")
    @ResponseBody
    ApiResult<List<PropertyKeyVo>> getPropertyList(ProTypeIdDto proTypeIdDto);
}
