package com.baibei.shiyi.admin.modules.system.service.impl;

import com.baibei.shiyi.admin.modules.system.dao.PermissionMapper;
import com.baibei.shiyi.admin.modules.system.model.Permission;
import com.baibei.shiyi.admin.modules.system.service.IPermissionService;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.List;


/**
 * @author: uqing
 * @date: 2019/08/07 09:05:52
 * @description: Permission服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PermissionServiceImpl extends AbstractService<Permission> implements IPermissionService {

    @Autowired
    private PermissionMapper tblAdminPermissionMapper;

    @Override
    public List<Permission> listByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return null;
        }
        Condition condition = new Condition(Permission.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andIn("id", ids);
        return findByCondition(condition);
    }
}
