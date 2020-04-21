package com.baibei.shiyi.user.service;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.user.model.CustomerTest;
import com.baibei.shiyi.common.core.mybatis.Service;


/**
* @author: hyc
* @date: 2019/11/28 11:10:36
* @description: CustomerTest服务接口
*/
public interface ICustomerTestService extends Service<CustomerTest> {

    ApiResult createUser();
}
