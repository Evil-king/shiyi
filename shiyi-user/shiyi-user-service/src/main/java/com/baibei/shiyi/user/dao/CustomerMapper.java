package com.baibei.shiyi.user.dao;

import com.baibei.shiyi.common.core.mybatis.MyMapper;
import com.baibei.shiyi.user.common.dto.CustomerPageListDto;
import com.baibei.shiyi.user.common.vo.CustomerPageListVo;
import com.baibei.shiyi.user.feign.bean.dto.AdminCustomerAccountDto;
import com.baibei.shiyi.user.feign.bean.dto.CustomerListDto;
import com.baibei.shiyi.user.feign.bean.vo.AdminCustomerBalanceVo;
import com.baibei.shiyi.user.feign.bean.vo.CustomerListVo;
import com.baibei.shiyi.user.model.Customer;

import java.util.List;

public interface CustomerMapper extends MyMapper<Customer> {
    List<CustomerPageListVo> pageList(CustomerPageListDto customerPageListDto);

    List<AdminCustomerBalanceVo> getCustomerBalanceList(AdminCustomerAccountDto adminCustomerAccountDto);

    List<CustomerListVo> getAllCustomerList(CustomerListDto customerListDto);
}