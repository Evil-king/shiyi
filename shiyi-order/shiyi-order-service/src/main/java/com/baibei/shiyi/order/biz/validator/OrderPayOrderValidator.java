package com.baibei.shiyi.order.biz.validator;

import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.exception.SystemException;
import com.baibei.shiyi.common.tool.validator2.ValidateException;
import com.baibei.shiyi.common.tool.validator2.Validator;
import com.baibei.shiyi.common.tool.validator2.ValidatorContext;
import com.baibei.shiyi.order.model.Order;
import com.baibei.shiyi.order.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/10/30 16:53
 * @description:
 */
@Component
@Slf4j
public class OrderPayOrderValidator implements Validator<String> {
    @Autowired
    private IOrderService orderService;

    @Override
    public void validate(ValidatorContext context, String s) {
        Order order = orderService.findByOrderNo(s);
        if (order == null) {
            log.info("订单{}不存在", s);
            throw new SystemException("订单不存在");
        }
        if (!Constants.MallOrderStatus.WAIT.equals(order.getStatus())) {
            throw new ValidateException("订单已处理");
        }
        context.setAttribute("order", order);
    }
}