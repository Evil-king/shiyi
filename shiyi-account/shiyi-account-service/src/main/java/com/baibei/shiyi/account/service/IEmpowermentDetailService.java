package com.baibei.shiyi.account.service;
import com.baibei.shiyi.account.model.EmpowermentDetail;
import com.baibei.shiyi.common.core.mybatis.Service;

import java.util.List;


/**
* @author: hyc
* @date: 2019/11/11 10:32:40
* @description: EmpowermentDetail服务接口
*/
public interface IEmpowermentDetailService extends Service<EmpowermentDetail> {
    /**
     * 找到所有的有效明细
     * @param customerNo
     * @return
     */
    List<EmpowermentDetail> findAllByCustomerNo(String customerNo);

    void updateMore(List<EmpowermentDetail> empowermentDetails);
}
