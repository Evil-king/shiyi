package com.baibei.shiyi.trade.web.admin;

import com.baibei.shiyi.account.feign.bean.dto.CustomerNoDto;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.trade.feign.bean.dto.FeeExemptionConfigDto;
import com.baibei.shiyi.trade.feign.bean.vo.FeeExemptionConfigVo;
import com.baibei.shiyi.trade.feign.client.admin.IAdminFeeExemptionConfigFeign;
import com.baibei.shiyi.trade.model.FeeExemptionConfig;
import com.baibei.shiyi.trade.service.IFeeExemptionConfigService;
import com.baibei.shiyi.trade.service.ITradeDayService;
import com.baibei.shiyi.user.feign.bean.vo.PABCustomerVo;
import com.baibei.shiyi.user.feign.client.shiyi.ICustomerFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

/**
 * 交易手续费豁免配置表
 */
@RestController
@RequestMapping("/shiyi/admin/trade/fee/exemption/config")
@Slf4j
public class AdminFeeExemptionConfigController implements IAdminFeeExemptionConfigFeign {

    @Autowired
    private IFeeExemptionConfigService feeExemptionConfigService;

    @Autowired
    private ICustomerFeign customerFeign;

    @Autowired
    private ITradeDayService tradeDayService;


    @Override
    public ApiResult<MyPageInfo<FeeExemptionConfigVo>> pageList(@Validated @RequestBody FeeExemptionConfigDto feeExemptionDto) {
        return ApiResult.success(feeExemptionConfigService.pageList(feeExemptionDto));
    }

    @Override
    public ApiResult add(@RequestBody FeeExemptionConfigDto tradeFeeExemptionDto) {
        if (StringUtils.isEmpty(tradeFeeExemptionDto.getCustomerNo()) ||
                StringUtils.isEmpty(tradeFeeExemptionDto.getCustomerName())) {
            return ApiResult.error("用户编号或者用户姓名不能为空");
        }
        if (!tradeDayService.isClose()) {
            return ApiResult.error("当前没有闭市，无法添加豁免名单");
        }
        Condition condition = new Condition(FeeExemptionConfig.class);
        Example.Criteria criteria = feeExemptionConfigService.buildValidCriteria(condition);
        criteria.andEqualTo("customerNo", tradeFeeExemptionDto.getCustomerNo());
        FeeExemptionConfig feeExemptionConfig = feeExemptionConfigService.findOneByCondition(condition);
        if (feeExemptionConfig != null) {
            return ApiResult.error("当前豁免用户已经存在");
        }
        CustomerNoDto customerNoDto = new CustomerNoDto();
        customerNoDto.setCustomerNo(tradeFeeExemptionDto.getCustomerNo());

        ApiResult<PABCustomerVo> customerResult = customerFeign.findCustomerNo(customerNoDto);
        if (customerResult.hasFail()) {
            return ApiResult.error("用户不存在");
        }
        feeExemptionConfigService.add(tradeFeeExemptionDto);
        return ApiResult.success();
    }

    @Override
    public ApiResult delete(@RequestBody FeeExemptionConfigDto tradeFeeExemptionDto) {
        if (tradeFeeExemptionDto.getId() == null) {
            return ApiResult.error("Id不能为空");
        }
        this.feeExemptionConfigService.softDeleteById(tradeFeeExemptionDto.getId());
        return ApiResult.success();
    }
}
