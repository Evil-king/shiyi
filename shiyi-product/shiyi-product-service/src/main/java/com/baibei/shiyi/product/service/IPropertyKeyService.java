package com.baibei.shiyi.product.service;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.product.feign.bean.dto.*;
import com.baibei.shiyi.product.feign.bean.vo.PropertyKeyVo;
import com.baibei.shiyi.product.model.PropertyKey;
import com.baibei.shiyi.common.core.mybatis.Service;

import java.util.List;


/**
* @author: Longer
* @date: 2019/07/30 10:12:45
* @description: PropertyKey服务接口
*/
public interface IPropertyKeyService extends Service<PropertyKey> {

    /**
     * 添加属性
     * @param addPropertyuDto
     */
    ApiResult addProperty(AddPropertyuDto addPropertyuDto);

    /**
     * 根据类目id找到具体的属性
     * @param id
     * @return
     */
    List<PropertyKey> findPropertyByTypeId(Long id);


    ApiResult<MyPageInfo<PropertyKeyVo>> propertyPageList(PropertyKeyDto propertyKeyDto);

    ApiResult updateProperty(UpdatePropertyDto updatePropertyDto);

    ApiResult deleteProperty(DeleteIdsDto deleteIdsDto);

    ApiResult<List<PropertyValueVo>> getValueByKeyId(Long id);

    ApiResult<List<PropertyKeyVo>> getPropertyList(ProTypeIdDto proTypeIdDto);

    /**
     * 更新编辑状态
     * @param propId
     * @param editFlag 是否可编辑标识
     */
    void modifyEditFlag(Long propId,String editFlag);
}
