package com.baibei.shiyi.admin.modules.system.service;
import com.baibei.shiyi.admin.modules.system.model.OrgTest;
import com.baibei.shiyi.common.core.mybatis.Service;


/**
* @author: hyc
* @date: 2019/11/28 15:57:22
* @description: OrgTest服务接口
*/
public interface IOrgTestService extends Service<OrgTest> {

    void createAgent();
}
