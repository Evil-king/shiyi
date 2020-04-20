package com.baibei.shiyi.admin;


import com.alibaba.fastjson.JSONObject;
import com.baibei.shiyi.admin.modules.system.bean.dto.RoleDto;
import com.baibei.shiyi.admin.modules.system.bean.dto.RoleGroupDto;
import com.baibei.shiyi.admin.modules.system.bean.dto.RolePageDto;
import com.baibei.shiyi.admin.modules.system.bean.vo.MenuVo;
import com.baibei.shiyi.admin.modules.system.model.Role;
import com.baibei.shiyi.admin.modules.system.service.IMenuService;
import com.baibei.shiyi.admin.modules.system.service.IRoleService;
import com.baibei.shiyi.common.tool.constants.Constants;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AdminWebApplication.class)
public class RoleControllerTest {

    protected MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext wac;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IMenuService menuService;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();  //初始化MockMvc对象
    }


    @Test
    public void testAddRoleGroup() throws Exception {
        RoleGroupDto roleGroupDto = new RoleGroupDto();
        roleGroupDto.setName("系统权限组");
        roleGroupDto.setSeq(1L);
        mockMvc.perform(post("/admin/role/addRoleGroup").contentType(MediaType.APPLICATION_JSON_UTF8).content(JSONObject.toJSONString(roleGroupDto))).andExpect(status().isOk());
        Role role = roleService.findBy("name", roleGroupDto.getName());
        Assert.assertNotNull(role);
    }

    @Test
    public void testAddRole() throws Exception {
        List<Role> roleList = this.findByRoleType(Constants.RoleType.GROUP);
        Assert.assertTrue((!roleList.isEmpty()));
        RoleDto roleDto = new RoleDto();
        roleDto.setPid(roleList.get(0).getId());
        roleDto.setName("市代理");
        roleDto.setSeq(0L);
        mockMvc.perform(post("/admin/role/addRole").contentType(MediaType.APPLICATION_JSON_UTF8).content(JSONObject.toJSONString(roleDto))).andExpect(status().isOk());
        Role role = roleService.findBy("name", roleDto.getName());
        Assert.assertNotNull(role);
    }

    @Test
    public void testPageList() throws Exception {
        RolePageDto rolePageDto = new RolePageDto();
        mockMvc.perform(post("/admin/role/pageList")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(JSONObject.toJSONString(rolePageDto))).andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testAddRoleMenu() throws Exception {
        List<MenuVo> menuList = menuService.findByMenuType(Constants.MenuType.MENU);
        Set<Long> menuIdList = menuList.stream().map(x -> x.getId()).collect(Collectors.toSet());
        List<Role> roleList = this.findByRoleType(Constants.RoleType.ONE);
        Assert.assertTrue((!roleList.isEmpty()));

        RoleDto roleDto = new RoleDto();
        roleDto.setId(roleList.get(0).getId());
        roleDto.setMenuId(menuIdList);
        mockMvc.perform(post("/admin/role/addRoleMenu").contentType(MediaType.APPLICATION_JSON_UTF8).content(JSONObject.toJSONString(roleDto))).andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
    }

    public List<Role> findByRoleType(String roleType) {
        Condition condition = new Condition(Role.class);
        condition.createCriteria().andEqualTo("roleType", roleType);
        return roleService.findByCondition(condition);
    }

}
