package com.baibei.shiyi.order.biz.validator;

import com.baibei.shiyi.account.feign.bean.dto.CustomerNoDto;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.enumeration.CustomerStatusEnum;
import com.baibei.shiyi.common.tool.exception.SystemException;
import com.baibei.shiyi.common.tool.validator2.ValidateException;
import com.baibei.shiyi.common.tool.validator2.Validator;
import com.baibei.shiyi.common.tool.validator2.ValidatorContext;
import com.baibei.shiyi.order.common.dto.OrderSubmitDto;
import com.baibei.shiyi.user.feign.bean.vo.CustomerVo;
import com.baibei.shiyi.user.feign.client.CustomerFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/10/30 19:24
 * @description:
 */
@Component
@Slf4j
public class OrderSubmitCustomerValidator implements Validator<OrderSubmitDto> {
    @Autowired
    private CustomerFeign customerFeign;

    @Override
    public void validate(ValidatorContext context, OrderSubmitDto orderSubmitDto) {
        CustomerNoDto dto = new CustomerNoDto();
        dto.setCustomerNo(orderSubmitDto.getCustomerNo());
        ApiResult<CustomerVo> apiResult = customerFeign.findUserByCustomerNo(dto);
        if (apiResult.hasFail() || apiResult.getData() == null) {
            throw new SystemException("获取客户信息失败");
        }
        CustomerVo customerVo = apiResult.getData();
        if (CustomerStatusEnum.LIMIT_PAY.getCode().equals(customerVo.getCustomerStatus())) {
            log.info("用户" + customerVo.getCustomerNo() + "被限制支付..");
            throw new ValidateException("支付失败，请联系客服");
        }
        context.setAttribute("customer", customerVo);
    }
}