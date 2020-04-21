package com.baibei.shiyi.pingan.service;

import com.baibei.shiyi.pingan.feign.base.dto.ReconciliationMatchingDto;
import com.baibei.shiyi.pingan.feign.base.vo.ReconciliationMatchingVo;

public interface IPABReconciliationMatching {


    ReconciliationMatchingVo reconciliationMatching(ReconciliationMatchingDto reconciliationMatchingDto);


}
