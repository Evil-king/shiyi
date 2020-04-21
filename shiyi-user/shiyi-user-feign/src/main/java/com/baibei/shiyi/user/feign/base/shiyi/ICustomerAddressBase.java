package com.baibei.shiyi.user.feign.base.shiyi;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.user.feign.bean.vo.CustomerAddressVo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author: hyc
 * @date: 2019/8/5 17:18
 * @description:
 */
public interface ICustomerAddressBase {
    /**
     * 通过用户编号获取用户地址
     */
    @RequestMapping("/shiyi/user/customerAddress")
    @ResponseBody
    ApiResult<List<CustomerAddressVo>> findAddressByCustomerNo(@RequestParam("customerNo")String customerNo);

    /**
     * 通过id获取用户地址
     */
    @RequestMapping("/shiyi/user/getCustomerAddressById")
    @ResponseBody
    ApiResult<CustomerAddressVo> getCustomerAddressById(@RequestParam("id")Long id);

    /**
     * 通过用户编号查询默认地址
     */
    @RequestMapping("/shiyi/user/getDefaultAddressByNo")
    @ResponseBody
    ApiResult<CustomerAddressVo> getDefaultAddressByNo(@RequestParam("customerNo")String customerNo);
}
