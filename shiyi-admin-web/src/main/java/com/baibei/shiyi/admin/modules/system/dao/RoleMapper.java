package com.baibei.shiyi.admin.modules.system.dao;

import com.baibei.shiyi.admin.modules.system.bean.dto.RolePageDto;
import com.baibei.shiyi.admin.modules.system.bean.vo.RoleVo;
import com.baibei.shiyi.admin.modules.system.model.Role;
import com.baibei.shiyi.common.core.mybatis.MyMapper;

import java.util.List;

public interface RoleMapper extends MyMapper<Role> {

    List<RoleVo> pageList(RolePageDto pageDto);
}