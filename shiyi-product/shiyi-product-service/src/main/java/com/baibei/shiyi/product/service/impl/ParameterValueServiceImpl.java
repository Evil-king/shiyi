package com.baibei.shiyi.product.service.impl;

import com.baibei.shiyi.product.dao.ParameterValueMapper;
import com.baibei.shiyi.product.model.ParameterValue;
import com.baibei.shiyi.product.service.IParameterValueService;
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
* @description: ParameterValue服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class ParameterValueServiceImpl extends AbstractService<ParameterValue> implements IParameterValueService {

    @Autowired
    private ParameterValueMapper tblProParameterValueMapper;

    @Override
    public void insertSelective(ParameterValue parameterValue) {
        tblProParameterValueMapper.insertSelective(parameterValue);
    }

    @Override
    public void deleteByKeyId(Long id) {
        Condition condition=new Condition(ParameterValue.class);
        Example.Criteria criteria=buildValidCriteria(condition);
        criteria.andEqualTo("keyId",id);
        tblProParameterValueMapper.deleteByCondition(condition);
    }

    @Override
    public List<ParameterValue> findByKeyId(Long id) {
        Condition condition=new Condition(ParameterValue.class);
        Example.Criteria criteria=buildValidCriteria(condition);
        criteria.andEqualTo("keyId",id);
        return tblProParameterValueMapper.selectByCondition(condition);
    }
}
