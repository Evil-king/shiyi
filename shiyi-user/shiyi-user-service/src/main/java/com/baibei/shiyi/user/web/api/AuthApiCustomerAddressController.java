package com.baibei.shiyi.user.web.api;

import com.baibei.shiyi.account.feign.bean.dto.CustomerNoDto;
import com.baibei.shiyi.common.core.aop.NoRepeatSubmit;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.user.common.dto.CustomerAddressDto;
import com.baibei.shiyi.user.feign.bean.dto.DeleteAddressDto;
import com.baibei.shiyi.user.feign.bean.vo.CustomerAddressVo;
import com.baibei.shiyi.user.service.ICustomerAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: hyc
 * @date: 2019/8/12 13:41
 * @description:
 */
@RestController
@RequestMapping("/auth/api/customer/customerAddress")
public class AuthApiCustomerAddressController {
    @Autowired
    private ICustomerAddressService customerAddressService;

    /**
     * 获取所有地址
     * @param customerNoDto
     * @return
     */
    @PostMapping("/getAllAddress")
    @ResponseBody
    public ApiResult<List<CustomerAddressVo>> getAllAddress(@RequestBody @Validated CustomerNoDto customerNoDto){
        return customerAddressService.findAddressByCustomerNo(customerNoDto.getCustomerNo());
    }

    @PostMapping("/updateAddress")
    @NoRepeatSubmit
    public ApiResult updateAddress(@RequestBody @Validated CustomerAddressDto customerAddressDto){
            if(!StringUtils.isEmpty(customerAddressDto.getId())){
                //如果ID不为空，则为修改地址，此时应该将原ID的地址改为删除状态（flag为0）
                ApiResult apiResult=customerAddressService.deleteAddress(customerAddressDto.getId(),customerAddressDto.getCustomerNo());
                if(!apiResult.hasSuccess()){
                    return ApiResult.error("修改地址信息失败");
                }
            }
            //如果此时这个地址为默认地址，那么则需要将原有的默认地址改为非默认地址
            if(customerAddressDto.getDefaultAddress()==true){
                customerAddressService.updateByCustomerAndDefaultAddress(customerAddressDto.getCustomerNo(),customerAddressDto.getDefaultAddress());
            }
            ApiResult result=customerAddressService.insertCustomerAddress(customerAddressDto);
            return result;
    }
    @PostMapping("/deleteAddress")
    @NoRepeatSubmit
    public ApiResult delelteAddress(@RequestBody @Validated DeleteAddressDto deleteAddressDto){
        return customerAddressService.deleteAddress(deleteAddressDto.getId(), deleteAddressDto.getCustomerNo());
    }
}
