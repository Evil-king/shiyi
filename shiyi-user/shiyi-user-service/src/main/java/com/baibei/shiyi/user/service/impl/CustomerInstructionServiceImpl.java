package com.baibei.shiyi.user.service.impl;

import com.baibei.shiyi.account.feign.bean.dto.CustomerNoDto;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.api.ResultEnum;
import com.baibei.shiyi.common.tool.utils.IdWorker;
import com.baibei.shiyi.user.dao.CustomerDetailMapper;
import com.baibei.shiyi.user.dao.CustomerInstructionMapper;
import com.baibei.shiyi.user.dao.CustomerMapper;
import com.baibei.shiyi.user.model.Customer;
import com.baibei.shiyi.user.model.CustomerDetail;
import com.baibei.shiyi.user.model.CustomerInstruction;
import com.baibei.shiyi.user.service.ICustomerInstructionService;
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
* @date: 2019/05/30 16:14:40
* @description: CustomerInstruction服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class CustomerInstructionServiceImpl extends AbstractService<CustomerInstruction> implements ICustomerInstructionService {

    @Autowired
    private CustomerInstructionMapper customerInstructionMapper;

    @Autowired
    private CustomerDetailMapper customerDetailMapper;

    @Autowired
    private CustomerMapper customerMapper;

    /**
     * 查询系统说明书
     * @return
     */
    @Override
    public ApiResult<String> findSysInstruction() {
        Condition condition=new Condition(CustomerInstruction.class);
        Example.Criteria criteria=buildValidCriteria(condition);
        criteria.andEqualTo("customerNo",0);
        List<CustomerInstruction> instructionList= customerInstructionMapper.selectByCondition(condition);
        if(instructionList.size()<1){
            return ApiResult.badParam("风险说明书未添加");
        }
        return ApiResult.success(instructionList.get(0).getText());
    }

    /**
     * 同意风险说明书
     * @param customerNoDto
     * @return
     */
    @Override
    public ApiResult<String> agreeInstruction(CustomerNoDto customerNoDto) {
        //通过交易商编码找到交易商用户的基本信息
        Condition condition=new Condition(CustomerDetail.class);
        Example.Criteria criteria=buildValidCriteria(condition);
        criteria.andEqualTo("customerNo",customerNoDto.getCustomerNo());
        List<CustomerDetail> customerDetails=customerDetailMapper.selectByCondition(condition);
        if(customerDetails.size()<1){
            return ApiResult.badParam("用户还未填写个人资料");
        }
        CustomerDetail customerDetail=customerDetails.get(0);
        //通过交易商编码找到用户，获取其中的手机号
        Condition customerCondition=new Condition(Customer.class);
        Example.Criteria customerCriteria=buildValidCriteria(customerCondition);
        customerCriteria.andEqualTo("customerNo",customerNoDto.getCustomerNo());
        List<Customer> customers=customerMapper.selectByCondition(condition);
        if(customers.size()<1){
            ApiResult apiResult=new ApiResult();
            apiResult.setCode(ResultEnum.ACCOUNT_NOT_EXIST.getCode());
            apiResult.setMsg(ResultEnum.ACCOUNT_NOT_EXIST.getMsg());
            return apiResult;
        }
        Customer customer=customers.get(0);
        //通过查找系统说明书方法找到系统说明书
        ApiResult<String> apiResult=findSysInstruction();
        //对数据进行一个一个替换
        String text=apiResult.getData().replaceFirst("name",customerDetail.getRealname()).replaceFirst("idCard",customerDetail.getIdcard()).replaceFirst("address",customerDetail.getAddress()).replaceFirst("mobile",customer.getMobile());
        //替换完成后进行插入用户的风险说明书
        CustomerInstruction instruction=new CustomerInstruction();
        instruction.setId(IdWorker.getId());
        instruction.setCustomerNo(customerNoDto.getCustomerNo());
        instruction.setCreateTime(new Date());
        instruction.setText(text);
        instruction.setModifyTime(new Date());
        instruction.setFlag(new Byte("1"));
        customerInstructionMapper.insertSelective(instruction);
        return ApiResult.success(text);
    }

//    public static void main(String[] args) {
//        String s="时间阿萨德群name若失去我后期维护idCard阿萨德健康清洁哦address大数据看得起mobile";
//        String texe=s.replaceFirst("name","1").replaceFirst("idCard","2").replaceFirst("address","3").replaceFirst("mobile","4");
//        System.out.println(texe);
//    }
}
