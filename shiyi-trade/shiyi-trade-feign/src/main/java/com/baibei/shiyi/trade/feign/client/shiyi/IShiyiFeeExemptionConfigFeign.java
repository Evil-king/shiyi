package com.baibei.shiyi.trade.feign.client.shiyi;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.trade.feign.bean.dto.FeeExemptionConfigDto;
import com.baibei.shiyi.trade.feign.bean.vo.FeeExemptionConfigVo;
import com.baibei.shiyi.trade.feign.client.hystrix.ShiyiFeeExemptionConfigHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 手续费豁免名单
 */
@FeignClient(value = "${shiyi-trade:shiyi-trade}",path = "/shiyi/fee/exemption/config",fallbackFactory = ShiyiFeeExemptionConfigHystrix.class)
public interface IShiyiFeeExemptionConfigFeign {

    /**
     * 查询豁免名单
     *
     * @param feeExemptionConfigDto
     * @return
     */
    @PostMapping(path = "/pageList")
    ApiResult<List<FeeExemptionConfigVo>> pageList(@RequestBody FeeExemptionConfigDto feeExemptionConfigDto);
}
