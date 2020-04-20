package com.baibei.shiyi.admin.modules.system.dao;

import com.baibei.shiyi.admin.modules.system.bean.dto.UserPageListDto;
import com.baibei.shiyi.admin.modules.system.bean.vo.UserPageListVo;
import com.baibei.shiyi.admin.modules.system.model.SysUser;
import com.baibei.shiyi.common.core.mybatis.MyMapper;

import java.util.List;

public interface SysUserMapper extends MyMapper<SysUser> {
    List<UserPageListVo> findListVo(UserPageListDto userPageListDto);
}