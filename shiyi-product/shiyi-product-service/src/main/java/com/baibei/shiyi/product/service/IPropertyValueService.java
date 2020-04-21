package com.baibei.shiyi.product.service;
import com.baibei.shiyi.product.model.PropertyValue;
import com.baibei.shiyi.common.core.mybatis.Service;

import java.util.List;


/**
* @author: Longer
* @date: 2019/07/30 10:12:45
* @description: PropertyValue服务接口
*/
public interface IPropertyValueService extends Service<PropertyValue> {
    /**
     * 根据keyID获取所有的属性可选值
     * @param keyId
     * @return
     */
    List<PropertyValue> findByKeyId(Long keyId);

    /**
     * 物理删除属性值
     * @param id
     */
    void deleteByKeyId(Long id);
}
