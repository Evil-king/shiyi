package com.baibei.shiyi.account.service.impl;

import com.baibei.shiyi.account.dao.EmpowermentDetailMapper;
import com.baibei.shiyi.account.model.EmpowermentDetail;
import com.baibei.shiyi.account.service.IEmpowermentDetailService;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.List;


/**
* @author: hyc
* @date: 2019/11/11 10:32:40
* @description: EmpowermentDetail服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class EmpowermentDetailServiceImpl extends AbstractService<EmpowermentDetail> implements IEmpowermentDetailService {

    @Autowired
    private EmpowermentDetailMapper tblAccountEmpowermentDetailMapper;

    @Override
    public List<EmpowermentDetail> findAllByCustomerNo(String customerNo) {
        Condition condition=new Condition(EmpowermentDetail.class);
        Example.Criteria criteria=buildValidCriteria(condition);
        criteria.andEqualTo("customerNo",customerNo);
        criteria.andEqualTo("status","enable");
        return tblAccountEmpowermentDetailMapper.selectByCondition(condition);
    }

    @Override
    public void updateMore(List<EmpowermentDetail> empowermentDetails) {
        tblAccountEmpowermentDetailMapper.updateMore(empowermentDetails);
    }
}
