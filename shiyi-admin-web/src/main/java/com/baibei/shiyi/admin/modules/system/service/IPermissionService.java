package com.baibei.shiyi.admin.modules.system.service;
import com.baibei.shiyi.admin.modules.system.model.Permission;
import com.baibei.shiyi.common.core.mybatis.Service;

import java.util.List;


/**
* @author: uqing
* @date: 2019/08/07 09:05:52
* @description: Permission服务接口
*/
public interface IPermissionService extends Service<Permission> {

    List<Permission> listByIds(List<Long> ids);
}
