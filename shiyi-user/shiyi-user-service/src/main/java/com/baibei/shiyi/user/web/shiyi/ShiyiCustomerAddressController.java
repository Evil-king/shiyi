package com.baibei.shiyi.user.web.shiyi;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.user.feign.base.shiyi.ICustomerAddressBase;
import com.baibei.shiyi.user.feign.bean.vo.CustomerAddressVo;
import com.baibei.shiyi.user.service.ICustomerAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author: hyc
 * @date: 2019/8/5 17:32
 * @description:
 */
@Controller
public class ShiyiCustomerAddressController implements ICustomerAddressBase {
    @Autowired
    private ICustomerAddressService customerAddressService;
    @Override
    public ApiResult<List<CustomerAddressVo>> findAddressByCustomerNo(@RequestParam("customerNo") String customerNo) {
        return customerAddressService.findAddressByCustomerNo(customerNo);
    }

    @Override
    public ApiResult<CustomerAddressVo> getCustomerAddressById(@RequestParam("id") Long id) {
        return customerAddressService.getCustomerAddressById(id);
    }

    @Override
    public ApiResult<CustomerAddressVo> getDefaultAddressByNo(String customerNo) {
        return customerAddressService.getDefaultAddressByNo(customerNo);
    }
}
