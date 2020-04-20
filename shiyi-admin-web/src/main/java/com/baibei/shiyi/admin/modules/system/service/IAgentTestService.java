package com.baibei.shiyi.admin.modules.system.service;
import com.baibei.shiyi.admin.modules.system.model.AgentTest;
import com.baibei.shiyi.common.core.mybatis.Service;


/**
* @author: hyc
* @date: 2019/11/28 15:57:22
* @description: AgentTest服务接口
*/
public interface IAgentTestService extends Service<AgentTest> {

    void createAgent();
}
