package com.baibei.shiyi.account.web.admin;

import com.baibei.shiyi.account.feign.base.admin.IAdminCustomerBeanBase;
import com.baibei.shiyi.account.feign.bean.dto.CustomerNoDto;
import com.baibei.shiyi.account.feign.bean.vo.CustomerBeanVo;
import com.baibei.shiyi.account.service.ICustomerBeanService;
import com.baibei.shiyi.common.tool.api.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: hyc
 * @date: 2019/11/1 16:14
 * @description:
 */
@RestController
public class AdminCustomerBeanController  implements IAdminCustomerBeanBase {
    @Autowired
    private ICustomerBeanService customerBeanService;
    @Override
    public ApiResult<CustomerBeanVo> getBeanBalance(@RequestParam("customerNo") String customerNo) {
        CustomerNoDto customerNoDto=new CustomerNoDto();
        customerNoDto.setCustomerNo(customerNo);
        return customerBeanService.getBalance(customerNoDto);
    }
}
