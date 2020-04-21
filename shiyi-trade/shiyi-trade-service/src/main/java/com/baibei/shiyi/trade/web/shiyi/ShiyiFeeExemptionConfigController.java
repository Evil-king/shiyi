package com.baibei.shiyi.trade.web.shiyi;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.trade.feign.bean.dto.FeeExemptionConfigDto;
import com.baibei.shiyi.trade.feign.bean.vo.FeeExemptionConfigVo;
import com.baibei.shiyi.trade.feign.client.shiyi.IShiyiFeeExemptionConfigFeign;
import com.baibei.shiyi.trade.service.IFeeExemptionConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/shiyi/fee/exemption/config")
public class ShiyiFeeExemptionConfigController implements IShiyiFeeExemptionConfigFeign {

    @Autowired
    private IFeeExemptionConfigService feeExemptionConfigService;

    @PostMapping(path = "/pageList")
    public ApiResult<List<FeeExemptionConfigVo>> pageList(@RequestBody FeeExemptionConfigDto feeExemptionConfigDto) {
        MyPageInfo<FeeExemptionConfigVo> result = feeExemptionConfigService.pageList(feeExemptionConfigDto);
        return ApiResult.success(result.getList());
    }
}
