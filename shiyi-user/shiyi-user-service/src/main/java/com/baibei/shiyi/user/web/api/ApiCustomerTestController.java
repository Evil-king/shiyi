package com.baibei.shiyi.user.web.api;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.user.service.ICustomerTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: hyc 测试类
 * @date: 2019/11/28 11:11
 * @description:
 */
@RestController
@RequestMapping("/api/customer/customerTest")
public class ApiCustomerTestController {
    @Autowired
    private ICustomerTestService customerTestService;

    @RequestMapping("/createUser")
    public ApiResult createUser(){
        return customerTestService.createUser();
    }
}
