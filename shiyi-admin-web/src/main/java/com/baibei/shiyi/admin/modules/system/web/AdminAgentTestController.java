package com.baibei.shiyi.admin.modules.system.web;

import com.baibei.shiyi.admin.modules.system.service.IAgentTestService;
import com.baibei.shiyi.admin.modules.system.service.IOrgTestService;
import com.baibei.shiyi.common.tool.api.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: hyc
 * @date: 2019/11/28 15:58
 * @description:
 */
@RestController
@RequestMapping("/admin/agentTest")
public class AdminAgentTestController {
    @Autowired
    private IAgentTestService agentTestService;

    @Autowired
    private IOrgTestService orgTestService;


    @RequestMapping("/createAgent")
    public ApiResult createAgent(){
        orgTestService.createAgent();
        agentTestService.createAgent();
        return ApiResult.success();
    }
}
