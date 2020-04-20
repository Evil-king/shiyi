package com.baibei.shiyi.admin.modules.trade.web;

import com.baibei.shiyi.account.feign.bean.dto.CustomerNoDto;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.trade.feign.bean.dto.FeeExemptionConfigDto;
import com.baibei.shiyi.trade.feign.bean.vo.FeeExemptionConfigVo;
import com.baibei.shiyi.trade.feign.client.admin.IAdminFeeExemptionConfigFeign;
import com.baibei.shiyi.user.feign.base.shiyi.ICustomerBase;
import com.baibei.shiyi.user.feign.bean.vo.CustomerVo;
import com.baibei.shiyi.user.feign.bean.vo.PABCustomerVo;
import com.baibei.shiyi.user.feign.client.shiyi.ICustomerFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 交易配置中，手续费豁免名单
 */
@RestController
@RequestMapping("/admin/trade/fee/exemption/config")
public class FeeExemptionConfigController {

    @Autowired
    private IAdminFeeExemptionConfigFeign feeExemptionConfigFeign;

    @Autowired
    private ICustomerFeign customerBase;

    @PostMapping(path = "/pageList")
    public ApiResult<MyPageInfo<FeeExemptionConfigVo>> pageList(@Validated @RequestBody FeeExemptionConfigDto feeExemptionConfigDto) {
        return feeExemptionConfigFeign.pageList(feeExemptionConfigDto);
    }

    @PostMapping(path = "/add")
    public ApiResult add(@RequestBody FeeExemptionConfigDto tradeFeeExemptionDto) {
        return feeExemptionConfigFeign.add(tradeFeeExemptionDto);
    }

    @PostMapping(path = "/delete")
    public ApiResult delete(@RequestBody FeeExemptionConfigDto tradeFeeExemptionDto) {
        return feeExemptionConfigFeign.delete(tradeFeeExemptionDto);
    }

    @PostMapping(path = "/findByCustomerName")
    public ApiResult<Map<String, Object>> findByCustomerName(@RequestBody FeeExemptionConfigDto tradeFeeExemptionDto) {
        if (StringUtils.isEmpty(tradeFeeExemptionDto.getCustomerNo())) {
            return ApiResult.error("客户编号不能为空");
        }
        CustomerNoDto customerNoDto = new CustomerNoDto();
        customerNoDto.setCustomerNo(tradeFeeExemptionDto.getCustomerNo());
        ApiResult<PABCustomerVo> customerVoApiResult = customerBase.findCustomerNo(customerNoDto);
        if (customerVoApiResult.hasFail()) {
            return ApiResult.error("获取用户姓名失败");
        }
        PABCustomerVo pabCustomerVo = customerVoApiResult.getData();
        Map<String, Object> result = new HashMap<>();
        result.put("customerNo", pabCustomerVo.getCustomerNo());
        result.put("customerName", pabCustomerVo.getRealName());
        return ApiResult.success(result);
    }

}
