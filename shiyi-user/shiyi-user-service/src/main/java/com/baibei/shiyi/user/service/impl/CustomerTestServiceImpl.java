package com.baibei.shiyi.user.service.impl;

import com.baibei.shiyi.account.feign.bean.dto.CustomerNoDto;
import com.baibei.shiyi.account.feign.client.AccountFeign;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.enumeration.CustomerStatusEnum;
import com.baibei.shiyi.common.tool.utils.IdWorker;
import com.baibei.shiyi.common.tool.utils.MD5Util;
import com.baibei.shiyi.common.tool.utils.NoUtil;
import com.baibei.shiyi.user.dao.CustomerDetailMapper;
import com.baibei.shiyi.user.dao.CustomerMapper;
import com.baibei.shiyi.user.dao.CustomerRefMapper;
import com.baibei.shiyi.user.dao.CustomerTestMapper;
import com.baibei.shiyi.user.model.Customer;
import com.baibei.shiyi.user.model.CustomerDetail;
import com.baibei.shiyi.user.model.CustomerRef;
import com.baibei.shiyi.user.model.CustomerTest;
import com.baibei.shiyi.user.service.ICustomerTestService;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
* @author: hyc
* @date: 2019/11/28 11:10:36
* @description: CustomerTest服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class CustomerTestServiceImpl extends AbstractService<CustomerTest> implements ICustomerTestService {
    @Value("${userPicture}")
    private String userPicture;

    @Autowired
    private CustomerTestMapper tblCustomerTestMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private AccountFeign accountFeign;

    @Autowired
    private CustomerRefMapper customerRefMapper;

    @Autowired
    private CustomerDetailMapper customerDetailMapper;

    @Override
    public ApiResult createUser() {
        Condition condition=new Condition(CustomerTest.class);
        Example.Criteria criteria=condition.createCriteria();
        criteria.andEqualTo("flag","1");
        List<String> customers=new ArrayList<>();
        List<CustomerTest> customerTests=tblCustomerTestMapper.selectByCondition(condition);
        for (int i = 0; i <customerTests.size() ; i++) {
            CustomerTest customerTest = customerTests.get(i);
            Customer customer=new Customer();
            customer.setId(IdWorker.getId());
            customer.setCityAgentCode(customerTest.getOrgCode());
            customer.setOrgCode(StringUtils.isEmpty(customerTest.getAgentCode())?null:Long.valueOf(customerTest.getAgentCode()));
            customer.setCustomerType(customerTest.getCustomerType());
            customer.setRecommenderId(StringUtils.isEmpty(customerTest.getRecommenderMobile())?"0":customerTest.getRecommenderMobile());
            customer.setOrgId(Long.valueOf(customerTest.getDirectlyCode()));
            int length = customer.getCityAgentCode().toString().length();

            String customerNO = null;//生成一个交易商编码
            while (true) {
                //生成交易编码，并循环判断该编码是否重复
                customerNO = customer.getCityAgentCode() + NoUtil.randomNumbers(12 - length);
                Customer findCustomer = new Customer();
                findCustomer.setCustomerNo(customerNO);
                findCustomer.setFlag(new Byte("1"));
                findCustomer = customerMapper.selectOne(findCustomer);
                if (null == findCustomer) {
                    break;
                }
            }
            customer.setCustomerNo(customerNO);
            String password=customerTest.getCustomerMobile().substring(customerTest.getCustomerMobile().length()-6,customerTest.getCustomerMobile().length());
            String salt=MD5Util.getRandomSalt(10);
            customer.setSalt(salt);
            customer.setPassword(MD5Util.md5Hex(password,salt));
            customer.setMobile(customerTest.getCustomerMobile());
            customer.setCustomerStatus(CustomerStatusEnum.NORMAL.getCode());
            customer.setSigning("0");
            customer.setIsSign("0");
            customer.setCreateTime(new Date());
            customer.setModifyTime(new Date());
            customer.setFlag(new Byte("1"));
            int insertSelective = customerMapper.insertSelective(customer);
            //插入一条关系记录表
            CustomerRef customerRef = new CustomerRef();
            customerRef.setId(IdWorker.getId());
            customerRef.setOrgId(customer.getOrgId());
            customerRef.setStartTime(new Date());
            customerRef.setRecommenderId(customer.getRecommenderId());
            customerRef.setCreateTime(new Date());
            customerRef.setCustomerNo(customer.getCustomerNo());
            customerRef.setFlag(new Byte("1"));
            customerRef.setModifyTime(new Date());
            customerRef.setOperationType(new Byte("1"));
            customerRef.setOperator(0L);
            customerRefMapper.insertSelective(customerRef);
            CustomerDetail customerDetail = new CustomerDetail();
            customerDetail.setId(IdWorker.getId());
            customerDetail.setCustomerNo(customerNO);
            customerDetail.setUserPicture(userPicture);
            customerDetail.setCreateTime(new Date());
            customerDetail.setModifyTime(new Date());
            customerDetail.setFlag(new Byte("1"));
            customerDetailMapper.insertSelective(customerDetail);
            //通过账户服务插入对应的记录
            customers.add(customer.getCustomerNo());
            customerTest.setFlag(0L);
            customerTest.setUpdateTime(new Date());
            tblCustomerTestMapper.updateByPrimaryKeySelective(customerTest);
        }
        accountFeign.registers(customers);
        return ApiResult.success();
    }
}
