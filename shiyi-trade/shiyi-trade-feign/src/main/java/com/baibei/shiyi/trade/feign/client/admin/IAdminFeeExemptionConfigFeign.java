package com.baibei.shiyi.trade.feign.client.admin;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.trade.feign.bean.dto.FeeExemptionConfigDto;
import com.baibei.shiyi.trade.feign.bean.vo.FeeExemptionConfigVo;
import com.baibei.shiyi.trade.feign.client.hystrix.AdminFeeExemptionConfigHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "${shiyi-trade:shiyi-trade}", path = "/shiyi/admin/trade/fee/exemption/config",fallbackFactory = AdminFeeExemptionConfigHystrix.class)
public interface IAdminFeeExemptionConfigFeign {


    /**
     * 分页查询
     *
     * @param feeExemptionDto
     * @return
     */
    @PostMapping(path = "/pageList")
    ApiResult<MyPageInfo<FeeExemptionConfigVo>> pageList(@Validated @RequestBody FeeExemptionConfigDto feeExemptionDto);

    /**
     * 添加对象
     *
     * @param tradeFeeExemptionDto
     * @return
     */
    @PostMapping(path = "/add")
    ApiResult add(@RequestBody FeeExemptionConfigDto tradeFeeExemptionDto);

    /**
     * 移除豁免名单
     *
     * @param tradeFeeExemptionDto
     * @return
     */
    @PostMapping(path = "/delete")
    ApiResult delete(@RequestBody FeeExemptionConfigDto tradeFeeExemptionDto);
}
