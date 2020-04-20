package com.baibei.shiyi.account.web.shiyi;

import com.baibei.shiyi.account.feign.base.shiyi.ICustomerBeanBase;
import com.baibei.shiyi.account.feign.bean.dto.ChangeCustomerBeanDto;
import com.baibei.shiyi.account.feign.bean.dto.CustomerNoDto;
import com.baibei.shiyi.account.feign.bean.vo.CustomerBeanVo;
import com.baibei.shiyi.account.service.ICustomerBeanService;
import com.baibei.shiyi.common.tool.api.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: hyc
 * @date: 2019/10/29 17:36
 * @description:
 */
@RestController
public class ShiyiCustomerBeanController implements ICustomerBeanBase {
    @Autowired
    private ICustomerBeanService customerBeanService;
    @Override
    public ApiResult<CustomerBeanVo> getBalance(@RequestBody @Validated  CustomerNoDto customerNoDto) {
        return customerBeanService.getBalance(customerNoDto);
    }

    @Override
    public ApiResult changeAmount(@RequestBody @Validated ChangeCustomerBeanDto changeAmountDto) {
        return customerBeanService.changeAmount(changeAmountDto);
    }

    @Override
    public ApiResult release() {
        return customerBeanService.dayRelease();
    }

}
