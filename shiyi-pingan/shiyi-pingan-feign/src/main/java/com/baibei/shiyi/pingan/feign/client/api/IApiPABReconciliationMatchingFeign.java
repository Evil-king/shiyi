package com.baibei.shiyi.pingan.feign.client.api;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.pingan.feign.base.dto.ReconciliationMatchingDto;
import com.baibei.shiyi.pingan.feign.base.vo.ReconciliationMatchingVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "${shiyi-pingan:shiyi-pingan}", path = "/api/pingan")
public interface IApiPABReconciliationMatchingFeign {
    /**
     * 发起出入金对账和会员开销户对账
     *
     * @param reconciliationMatchingDto
     * @return
     */
    @PostMapping(path = "/reconciliationMatching")
    ApiResult<ReconciliationMatchingVo> reconciliationMatching(@Validated @RequestBody ReconciliationMatchingDto reconciliationMatchingDto);
}
