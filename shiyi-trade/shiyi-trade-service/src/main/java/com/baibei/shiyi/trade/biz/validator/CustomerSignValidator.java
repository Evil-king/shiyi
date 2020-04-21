package com.baibei.shiyi.trade.biz.validator;

import com.baibei.shiyi.account.feign.bean.dto.CustomerNoDto;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.api.ResultEnum;
import com.baibei.shiyi.common.tool.exception.SystemException;
import com.baibei.shiyi.common.tool.validator2.ValidateException;
import com.baibei.shiyi.common.tool.validator2.Validator;
import com.baibei.shiyi.common.tool.validator2.ValidatorContext;
import com.baibei.shiyi.user.feign.bean.vo.CustomerVo;
import com.baibei.shiyi.user.feign.client.CustomerFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/12/25 16:11
 * @description: 客户实名&&开户验证器
 */
@Slf4j
@Component
public class CustomerSignValidator implements Validator<String> {
    @Autowired
    private CustomerFeign customerFeign;

    @Override
    public void validate(ValidatorContext context, String s) {
        CustomerNoDto customerNoDto = new CustomerNoDto();
        customerNoDto.setCustomerNo(s);
        ApiResult<CustomerVo> apiResult = customerFeign.findUserByCustomerNo(customerNoDto);
        if (apiResult.hasFail()) {
            log.info("获取客户信息失败，apiResult={}", apiResult.toString());
            throw new SystemException("获取客户信息失败");
        }
        CustomerVo customerVo = apiResult.getData();
        if (customerVo == null) {
            log.info("获取客户信息为空");
            throw new SystemException("获取客户信息失败");
        }
        // 判断是否实名
        if ("0".equals(customerVo.getRealnameVerification())) {
            throw new ValidateException(ResultEnum.REALNAMEVERIFICATION_NOT);
        }
        // 判断是否开户
        if ("0".equals(customerVo.getSigning())) {
            throw new ValidateException(ResultEnum.SIGNING_NOT);
        }
        // 将客户信息存入上下文
        context.setAttribute("customerVo", customerVo);
    }
}