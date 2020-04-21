package com.baibei.shiyi.user.service.impl;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.utils.BeanUtil;
import com.baibei.shiyi.common.tool.utils.IdWorker;
import com.baibei.shiyi.user.common.dto.CustomerAddressDto;
import com.baibei.shiyi.user.dao.CustomerAddressMapper;
import com.baibei.shiyi.user.feign.bean.vo.CustomerAddressVo;
import com.baibei.shiyi.user.model.CustomerAddress;
import com.baibei.shiyi.user.service.ICustomerAddressService;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;


/**
* @author: hyc
* @date: 2019/08/05 17:26:50
* @description: CustomerAddress服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class CustomerAddressServiceImpl extends AbstractService<CustomerAddress> implements ICustomerAddressService {

    @Autowired
    private CustomerAddressMapper tblCustomerAddressMapper;

    @Override
    public ApiResult<List<CustomerAddressVo>> findAddressByCustomerNo(String customerNo) {
        Condition condition=new Condition(CustomerAddress.class);
        condition.setOrderByClause("default_address desc,create_time desc");
        Example.Criteria criteria=buildValidCriteria(condition);
        criteria.andEqualTo("customerNo",customerNo);
        List<CustomerAddress> customerAddresses=tblCustomerAddressMapper.selectByCondition(condition);
        List<CustomerAddressVo> customerAddressVos=BeanUtil.copyProperties(customerAddresses,CustomerAddressVo.class);
        return ApiResult.success(customerAddressVos);
    }

    @Override
    public ApiResult<CustomerAddressVo> getCustomerAddressById(Long id) {
        Condition condition=new Condition(CustomerAddress.class);
        Example.Criteria criteria=buildValidCriteria(condition);
        criteria.andEqualTo("id",id);
        List<CustomerAddress> customerAddresses=tblCustomerAddressMapper.selectByCondition(condition);
        if(customerAddresses.size()>0){
            CustomerAddressVo customerAddressVo=BeanUtil.copyProperties(customerAddresses.get(0),CustomerAddressVo.class);
            return ApiResult.success(customerAddressVo);
        }else {
            return ApiResult.badParam("地址信息不存在");
        }
    }

    @Override
    public ApiResult insertCustomerAddress(CustomerAddressDto customerAddressDto) {
        CustomerAddress address=BeanUtil.copyProperties(customerAddressDto,CustomerAddress.class);
        address.setId(IdWorker.getId());
        address.setCreateTime(new Date());
        address.setModifyTime(new Date());
        address.setFlag(new Byte("1"));
        Integer i=tblCustomerAddressMapper.insertSelective(address);
        if(i==1){
            return ApiResult.success();
        }else {
            return ApiResult.error("插入地址失败！");
        }
    }

    @Override
    public ApiResult deleteAddress(Long id, String customerNo) {
        Condition condition=new Condition(CustomerAddress.class);
        Example.Criteria criteria=buildValidCriteria(condition);
        criteria.andEqualTo("id",id);
        criteria.andEqualTo("customerNo",customerNo);
        CustomerAddress customerAddress=new CustomerAddress();
        customerAddress.setId(id);
        customerAddress.setFlag(new Byte("0"));
        customerAddress.setModifyTime(new Date());
        int i = tblCustomerAddressMapper.updateByConditionSelective(customerAddress,condition);
        if(i==1){
            return ApiResult.success();
        }else {
            return ApiResult.error("修改失败");
        }
    }

    @Override
    public void updateByCustomerAndDefaultAddress(String customerNo, Boolean defaultAddress) {
        Condition condition=new Condition(CustomerAddress.class);
        Example.Criteria criteria=buildValidCriteria(condition);
        criteria.andEqualTo("customerNo",customerNo);
        criteria.andEqualTo("defaultAddress",defaultAddress);
        List<CustomerAddress> customerAddresseList=tblCustomerAddressMapper.selectByCondition(condition);
        for (int i = 0; i < customerAddresseList.size(); i++) {
            CustomerAddress customerAddress=customerAddresseList.get(i);
            customerAddress.setDefaultAddress(false);
            customerAddress.setModifyTime(new Date());
            tblCustomerAddressMapper.updateByPrimaryKeySelective(customerAddress);
        }
    }

    @Override
    public ApiResult<CustomerAddressVo> getDefaultAddressByNo(String customerNo) {
        Condition condition=new Condition(CustomerAddress.class);
        Example.Criteria criteria=buildValidCriteria(condition);
        criteria.andEqualTo("customerNo",customerNo);
        criteria.andEqualTo("defaultAddress",1);
        List<CustomerAddress> customerAddresses=tblCustomerAddressMapper.selectByCondition(condition);
        if(customerAddresses.size()>0){
            return ApiResult.success(BeanUtil.copyProperties(customerAddresses.get(0),CustomerAddressVo.class));
        }else {
            return ApiResult.success();
        }

    }
}
