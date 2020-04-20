package com.baibei.shiyi.account.dao;

import com.baibei.shiyi.account.model.EmpowermentDetail;
import com.baibei.shiyi.common.core.mybatis.MyMapper;

import java.util.List;

public interface EmpowermentDetailMapper extends MyMapper<EmpowermentDetail> {
    void updateMore(List<EmpowermentDetail> empowermentDetails);
}