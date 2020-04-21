package com.baibei.shiyi.product.service.impl;

import com.baibei.shiyi.product.dao.PropertyValueMapper;
import com.baibei.shiyi.product.model.PropertyValue;
import com.baibei.shiyi.product.service.IPropertyValueService;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.List;


/**
* @author: Longer
* @date: 2019/07/30 10:12:45
* @description: PropertyValue服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class PropertyValueServiceImpl extends AbstractService<PropertyValue> implements IPropertyValueService {

    @Autowired
    private PropertyValueMapper tblProPropertyValueMapper;

    @Override
    public List<PropertyValue> findByKeyId(Long keyId) {
        Condition condition=new Condition(PropertyValue.class);
        condition.setOrderByClause("create_time desc");
        Example.Criteria criteria=buildValidCriteria(condition);
        criteria.andEqualTo("keyId",keyId);
        return tblProPropertyValueMapper.selectByCondition(condition);
    }

    @Override
    public void deleteByKeyId(Long id) {
        Condition condition=new Condition(PropertyValue.class);
        Example.Criteria criteria=buildValidCriteria(condition);
        criteria.andEqualTo("keyId",id);
        tblProPropertyValueMapper.deleteByCondition(condition);
    }
}
