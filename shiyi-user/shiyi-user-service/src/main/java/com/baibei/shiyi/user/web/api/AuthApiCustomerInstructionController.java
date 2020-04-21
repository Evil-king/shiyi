package com.baibei.shiyi.user.web.api;

import com.baibei.shiyi.account.feign.bean.dto.CustomerNoDto;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.user.service.ICustomerInstructionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: hyc
 * @date: 2019/5/30 14:20
 * @description:
 */
@Controller
@RequestMapping("/auth/api/customer/instruction")
public class AuthApiCustomerInstructionController {
    @Autowired
    private ICustomerInstructionService customerInstructionService;

    /**
     * 获取系统提供的风险说明书
     * @return
     */
    @PostMapping("/findSysInstruction")
    @ResponseBody
    public ApiResult<String> findSysInstruction(){
        return customerInstructionService.findSysInstruction();
    }

    /**
     * 同意风险说明书
     * @param customerNoDto
     * @return  同意后进行修改后的说明书
     */
    @PostMapping("/agreeInstruction")
    @ResponseBody
    public ApiResult<String> agreeInstruction(@RequestBody @Validated CustomerNoDto customerNoDto){
        return customerInstructionService.agreeInstruction(customerNoDto);
    }
}
