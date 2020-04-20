package com.baibei.shiyi.account.web.api;

import com.baibei.shiyi.account.feign.bean.dto.CustomerNoDto;
import com.baibei.shiyi.account.model.SigningRecord;
import com.baibei.shiyi.account.service.ISigningRecordService;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.api.ResultEnum;
import com.baibei.shiyi.common.tool.bean.CustomerBaseDto;
import com.baibei.shiyi.content.feign.base.IContentBase;
import com.baibei.shiyi.content.feign.bean.vo.ConfigurationContentVo;
import com.baibei.shiyi.user.feign.bean.vo.PABCustomerVo;
import com.baibei.shiyi.user.feign.client.shiyi.ICustomerFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 出金的公告配置
 */
@RestController
@RequestMapping("/auth/api/account/signing")
@Slf4j
public class AuthApiSigningRecordController {

    @Autowired
    private ISigningRecordService signingRecordService;

    @Autowired
    private ICustomerFeign customerFeign;

    @Autowired
    private IContentBase contentBase;


    /**
     * 获取签约信息
     *
     * @return
     */
    @PostMapping(path = "/getSigningInfo")
    public ApiResult<Map<String, Object>> getSigningInfo(@RequestBody CustomerBaseDto customerBaseDto) {
        CustomerNoDto customerNoDto = new CustomerNoDto();
        customerNoDto.setCustomerNo(customerBaseDto.getCustomerNo());
        ApiResult<PABCustomerVo> pabCustomerVoApiResult = customerFeign.findCustomerNo(customerNoDto);
        if (pabCustomerVoApiResult.hasFail()) {
            return ApiResult.error();
        }
        Map<String, Object> params = new HashMap<>();
        PABCustomerVo pabCustomerVo = pabCustomerVoApiResult.getData();

        params.put("bankCode", pabCustomerVo.getBankClientNo());
        params.put("acctName", pabCustomerVo.getRealName());

        ApiResult<ConfigurationContentVo> configurationContent = contentBase.getConfigurationContent();
        if (!configurationContent.getCode().equals(ResultEnum.SUCCESS.getCode())) {
            return ApiResult.error("系统错误");
        }
        ConfigurationContentVo configurationContentVo = configurationContent.getData();
        params.put("bankName", configurationContentVo.getBankName());

        params.put("receiptAccountNumber", configurationContentVo.getAccountNo());
        params.put("receiptAccount", configurationContentVo.getAccountName());
        params.put("bankAccount", configurationContentVo.getBankName());
        params.put("remarks", configurationContentVo.getInContent().replaceAll("\\\\n", "\n"));
        return ApiResult.success(params);
    }


}
