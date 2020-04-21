package com.baibei.shiyi.product.service;
import com.baibei.shiyi.product.model.ParameterValue;
import com.baibei.shiyi.common.core.mybatis.Service;

import java.util.List;


/**
* @author: Longer
* @date: 2019/07/30 10:12:45
* @description: ParameterValue服务接口
*/
public interface IParameterValueService extends Service<ParameterValue> {

    void insertSelective(ParameterValue parameterValue);

    void deleteByKeyId(Long id);

    List<ParameterValue> findByKeyId(Long id);
}
