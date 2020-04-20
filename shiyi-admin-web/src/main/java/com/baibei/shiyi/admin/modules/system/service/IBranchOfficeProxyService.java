package com.baibei.shiyi.admin.modules.system.service;

import com.baibei.shiyi.admin.modules.system.model.BranchOfficeProxy;
import com.baibei.shiyi.common.core.mybatis.Service;


/**
 * @author: hyc
 * @date: 2019/10/10 09:51:43
 * @description: BranchOfficeProxy服务接口
 */
public interface IBranchOfficeProxyService extends Service<BranchOfficeProxy> {

    void deleteByBranchCompanyId(Long branchCompanyId);
}
