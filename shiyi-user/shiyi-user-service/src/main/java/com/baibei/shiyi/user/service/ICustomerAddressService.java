package com.baibei.shiyi.user.service;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.user.common.dto.CustomerAddressDto;
import com.baibei.shiyi.user.feign.bean.vo.CustomerAddressVo;
import com.baibei.shiyi.user.model.CustomerAddress;
import com.baibei.shiyi.common.core.mybatis.Service;

import java.util.List;


/**
* @author: hyc
* @date: 2019/08/05 17:26:50
* @description: CustomerAddress服务接口
*/
public interface ICustomerAddressService extends Service<CustomerAddress> {
    /**
     * 通过用户编号获取用户地址
     * @param customerNo
     * @return
     */
    ApiResult<List<CustomerAddressVo>> findAddressByCustomerNo(String customerNo);

    /**
     * 通过id获取用户地址
     * @param id
     * @return
     */
    ApiResult<CustomerAddressVo> getCustomerAddressById(Long id);

    /**
     * 插入一条地址信息
     * @param customerAddressDto
     * @return
     */
    ApiResult insertCustomerAddress(CustomerAddressDto customerAddressDto);

    /**
     * 刪除地址（flag为0 软删除）
     * @param id
     * @param customerNo
     * @return
     */
    ApiResult deleteAddress(Long id, String customerNo);

    void updateByCustomerAndDefaultAddress(String customerNo, Boolean defaultAddress);

    ApiResult<CustomerAddressVo> getDefaultAddressByNo(String customerNo);
}
