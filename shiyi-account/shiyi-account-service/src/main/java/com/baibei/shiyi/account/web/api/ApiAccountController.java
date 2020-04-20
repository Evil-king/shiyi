package com.baibei.shiyi.account.web.api;

import com.baibei.shiyi.account.service.IAccountService;
import com.baibei.shiyi.account.service.ICustomerBeanService;
import com.baibei.shiyi.common.tool.api.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * @author: hyc
 * @date: 2019/5/31 9:55
 * @description:
 */
@Controller
@RequestMapping("/api/account")
public class ApiAccountController {

    @Autowired
    private ICustomerBeanService customerBeanService;

    @Autowired
    private IAccountService accountService;
    @RequestMapping("/release")
    public ApiResult release(){
        customerBeanService.dayRelease();
        return ApiResult.success();
    }



}
