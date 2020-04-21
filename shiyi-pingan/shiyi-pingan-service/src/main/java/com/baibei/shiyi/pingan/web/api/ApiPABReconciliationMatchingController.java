package com.baibei.shiyi.pingan.web.api;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.pingan.feign.base.dto.ReconciliationMatchingDto;
import com.baibei.shiyi.pingan.feign.base.vo.ReconciliationMatchingVo;
import com.baibei.shiyi.pingan.feign.client.api.IApiPABReconciliationMatchingFeign;
import com.baibei.shiyi.pingan.service.IPABReconciliationMatching;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 出入金流水对账及会员开销户流水匹配
 */
@RestController
@RequestMapping("/api/pingan")
public class ApiPABReconciliationMatchingController implements IApiPABReconciliationMatchingFeign {

    @Autowired
    private IPABReconciliationMatching reconciliationMatching;

    public ApiResult<ReconciliationMatchingVo> reconciliationMatching(@Validated @RequestBody ReconciliationMatchingDto reconciliationMatchingDto) {
        ReconciliationMatchingVo reconciliationMatchingVo = reconciliationMatching.reconciliationMatching(reconciliationMatchingDto);
        return ApiResult.success(reconciliationMatchingVo);
    }
}
