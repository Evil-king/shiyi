package com.baibei.shiyi.admin.modules.system.service;

import com.baibei.shiyi.admin.modules.system.model.OrganizationInfomation;
import com.baibei.shiyi.common.core.mybatis.Service;


/**
 * @author: hyc
 * @date: 2019/10/10 09:51:43
 * @description: OrganizationInfomation服务接口
 */
public interface IOrganizationInfomationService extends Service<OrganizationInfomation> {

    OrganizationInfomation findByOrganizationId(Long organizationId);
}
