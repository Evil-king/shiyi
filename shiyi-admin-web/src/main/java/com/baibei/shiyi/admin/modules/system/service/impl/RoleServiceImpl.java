package com.baibei.shiyi.admin.modules.system.service.impl;

import com.baibei.shiyi.admin.modules.system.bean.dto.RoleDto;
import com.baibei.shiyi.admin.modules.system.bean.dto.RoleGroupDto;
import com.baibei.shiyi.admin.modules.system.bean.dto.RolePageDto;
import com.baibei.shiyi.admin.modules.system.bean.vo.ChooseRuleVo;
import com.baibei.shiyi.admin.modules.system.bean.vo.RoleVo;
import com.baibei.shiyi.admin.modules.system.dao.RoleMapper;
import com.baibei.shiyi.admin.modules.system.model.Role;
import com.baibei.shiyi.admin.modules.system.model.RoleMenu;
import com.baibei.shiyi.admin.modules.system.service.IRoleMenuService;
import com.baibei.shiyi.admin.modules.system.service.IRoleService;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.exception.ServiceException;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.common.tool.page.PageParam;
import com.baibei.shiyi.common.tool.utils.BeanUtil;
import com.baibei.shiyi.common.tool.utils.IdWorker;
import com.github.pagehelper.PageHelper;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Condition;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author: uqing
 * @date: 2019/08/07 09:05:52
 * @description: Role服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RoleServiceImpl extends AbstractService<Role> implements IRoleService {

    @Autowired
    private RoleMapper tblAdminRoleMapper;

    @Autowired
    private IRoleMenuService roleMenuService;

    @Override
    public void addRoleGroup(RoleGroupDto roleGroupDto) {
        if (this.repeatVerification("name", roleGroupDto.getName(), null)) {
            throw new ServiceException("角色组名不能重复");
        }
        Role role = new Role();
        role.setId(IdWorker.getId());
        role.setPid(0L);
        role.setName(roleGroupDto.getName());
        role.setCreateTime(new Date());
        role.setModifyTime(new Date());
        role.setSeq(roleGroupDto.getSeq());
        role.setRoleType(Constants.RoleType.GROUP);
        role.setFlag(new Byte(Constants.Flag.VALID));
        this.save(role);
    }

    @Override
    public void addRole(RoleDto roleDto) {
        if (this.repeatVerification("name", roleDto.getName(), null)) {
            throw new ServiceException("角色名不能重复");
        }
        Role role = new Role();
        role.setPid(roleDto.getPid());
        role.setId(IdWorker.getId());
        role.setName(roleDto.getName());
        role.setRoleType(Constants.RoleType.ONE);
        role.setModifyTime(new Date());
        role.setCreateTime(new Date());
        role.setSeq(roleDto.getSeq());
        role.setFlag(new Byte(Constants.Flag.VALID));
        this.save(role);
    }

    @Override
    public void updateRoleGroup(RoleGroupDto roleGroupDto) {
        if (this.repeatVerification("name", roleGroupDto.getName(), roleGroupDto.getId())) {
            throw new ServiceException("角色组名不能重复");
        }
        Role role = new Role();
        role.setId(roleGroupDto.getId());
        role.setSeq(roleGroupDto.getSeq());
        role.setName(roleGroupDto.getName());
        role.setRoleType(Constants.RoleType.GROUP);
        role.setModifyTime(new Date());
        this.update(role);
    }

    @Override
    public void updateRole(RoleDto roleDto) {
        if (this.repeatVerification("name", roleDto.getName(), roleDto.getId())) {
            throw new ServiceException("角色名不能重复");
        }
        Role role = new Role();
        role.setId(roleDto.getId());
        role.setSeq(roleDto.getSeq());
        role.setName(roleDto.getName());
        role.setRoleType(Constants.RoleType.ONE);
        role.setModifyTime(new Date());
        this.update(role);
    }

    @Override
    public List<RoleVo> pageList(RolePageDto rolePageDto) {
        if (rolePageDto.getCurrentPage() != null && rolePageDto.getPageSize() != null) {
            PageHelper.startPage(rolePageDto.getCurrentPage(), rolePageDto.getPageSize());
        }
        List<RoleVo> roleVoList = this.tblAdminRoleMapper.pageList(rolePageDto);
        for (RoleVo roleVo : roleVoList) {
            roleVo.setMenuId(roleMenuService.getMenuIdList(roleVo.getId()));
            RolePageDto pageDto = new RolePageDto();
            pageDto.setPid(roleVo.getId());
            roleVo.setChildList(pageList(pageDto));
        }
        return roleVoList;
    }

    @Override
    public MyPageInfo<RoleVo> pageInfo(RolePageDto rolePageDto) {
        return new MyPageInfo<>(this.pageList(rolePageDto));
    }


    @Override
    public void deleteByRole(Long id) {
        Role role = this.findById(id);
        if (role == null) {
            throw new ServiceException("角色不存在");
        }
        List<Role> roleList = this.findByPid(role.getId());
        if (!roleList.isEmpty()) {
            throw new ServiceException("当前角色还有子角色无法删除");
        }
        // stop 1 删除角色
        this.deleteById(id);
        // stop2 删除角色的菜单
        roleMenuService.deleteByRoleId(id);
    }

    @Override
    public List<Role> findByPid(Long pid) {
        Condition constants = new Condition(Role.class);
        constants.createCriteria().andEqualTo("pid", pid);
        return this.findByCondition(constants);
    }

    @Override
    public void addRoleMenu(RoleDto roleDto) {
        //stop1 先删除角色关联的菜单,然后在添加
        roleMenuService.deleteByRoleId(roleDto.getId());
        for (Long menuId : roleDto.getMenuId()) {
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(roleDto.getId());
            roleMenu.setMenuId(menuId);
            roleMenu.setId(IdWorker.getId());
            this.roleMenuService.save(roleMenu);
        }
    }

    @Override
    public List<ChooseRuleVo> getAll() {
        List<Role> roles = tblAdminRoleMapper.selectAll();
        List<ChooseRuleVo> chooseRuleVos = new ArrayList<>();
        for (int i = 0; i < roles.size(); i++) {
            ChooseRuleVo chooseRuleVo = new ChooseRuleVo();
            chooseRuleVo.setRoleId(roles.get(i).getId());
            chooseRuleVo.setRoleName(roles.get(i).getName());
            chooseRuleVos.add(chooseRuleVo);
        }
        return chooseRuleVos;
    }
}
