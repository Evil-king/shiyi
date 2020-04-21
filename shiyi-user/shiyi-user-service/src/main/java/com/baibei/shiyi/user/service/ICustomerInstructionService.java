package com.baibei.shiyi.user.service;
import com.baibei.shiyi.account.feign.bean.dto.CustomerNoDto;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.user.model.CustomerInstruction;
import com.baibei.shiyi.common.core.mybatis.Service;


/**
* @author: hyc
* @date: 2019/05/30 16:14:40
* @description: CustomerInstruction服务接口
*/
public interface ICustomerInstructionService extends Service<CustomerInstruction> {

    ApiResult<String> findSysInstruction();

    ApiResult<String> agreeInstruction(CustomerNoDto customerNoDto);

}
