package com.baibei.shiyi.admin.modules.system.service;
import com.baibei.shiyi.admin.modules.system.model.RolesPermissions;
import com.baibei.shiyi.common.core.mybatis.Service;

import java.util.List;


/**
* @author: uqing
* @date: 2019/08/07 09:05:52
* @description: RolesPermissions服务接口
*/
public interface IRolesPermissionsService extends Service<RolesPermissions> {

    /**
     *查看某个权限是否有被引用
     * @param permissionId
     * @return
     */
    List<RolesPermissions> getRefByPermissionId(Long permissionId);


    List<Long> getPermissionList(List<Long> roleIdList);
}
