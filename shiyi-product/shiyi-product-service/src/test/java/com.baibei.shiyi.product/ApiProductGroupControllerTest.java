package com.baibei.shiyi.product;

import com.alibaba.fastjson.JSONObject;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.product.feign.bean.dto.GroupCurdDto;
import com.baibei.shiyi.product.feign.bean.dto.GroupDto;
import com.baibei.shiyi.product.feign.bean.dto.ProductGroupDto;
import com.baibei.shiyi.product.feign.bean.vo.GroupProductVo;
import com.baibei.shiyi.product.model.Group;
import com.baibei.shiyi.product.model.ProductGroupRef;
import com.baibei.shiyi.product.service.IGroupService;
import com.baibei.shiyi.product.service.IProductGroupRefService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import tk.mybatis.mapper.entity.Condition;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ProductServiceApplication.class)
@Transactional
@Slf4j
public class ApiProductGroupControllerTest {


    protected MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext wac;

    @Autowired
    private IGroupService groupService;

    @Autowired
    private IProductGroupRefService productGroupRefService;

    @Before()  //这个方法在每个方法执行之前都会执行一遍
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();  //初始化MockMvc对象

    }

    @Test
    public void testPageList() throws Exception {
        GroupDto groupDto = new GroupDto();
        groupDto.setCurrentPage(1);
        groupDto.setPageSize(10);
//        groupDto.setStartTime(new Date());
//        groupDto.setEndTime(new Date());
        mockMvc.perform(post("/admin/product/group/pageList").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSONObject.toJSONString(groupDto))).andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }


    @Test
    public void testGetById() {
        groupService.findAll().stream().forEach(
                result -> {
                    try {
                        mockMvc.perform(post("/admin/product/group/getById").contentType(MediaType.APPLICATION_JSON_UTF8)
                                .content(JSONObject.toJSONString(result))).andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        );
    }


    @Test
    public void testProductGroupProcess() throws Exception {
        // stop1 创建商品分组
        log.info("创建商品分组开始");
        GroupCurdDto groupDto = new GroupCurdDto();
        groupDto.setGroupType(Constants.GroupType.COMMON);
        groupDto.setTitle("手机");
        mockMvc.perform(post("/admin/product/group/save").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSONObject.toJSONString(groupDto))).andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());

        // stop2 查询分组不存在的商品
        log.info("查询分组不存在的商品");
        Group group = groupService.findByTitle(groupDto.getTitle());
        ProductGroupDto productGroupDto = new ProductGroupDto();
        productGroupDto.setCurrentPage(1);
        productGroupDto.setPageSize(10);
        productGroupDto.setGroupId(group.getId());

        MvcResult result = mockMvc.perform(post("/admin/product/group/findNoExistProductGroup")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(JSONObject.toJSONString(productGroupDto))).andExpect(status().isOk())
                .andReturn();
        JSONObject jsonObject = JSONObject.parseObject(result.getResponse().getContentAsString());
        JSONObject data = jsonObject.getJSONObject("data");
        List<GroupProductVo> productVo = JSONObject.parseArray(data.getString("list"), GroupProductVo.class);
        Set<String> shelfIds = productVo.stream().map(shelf -> shelf.getShelfId().toString()).collect(Collectors.toSet());
        // stop3 添加商品到商品分组中
        log.info("修改分组的商品");
        groupDto.setShelfIds(shelfIds);
        groupDto.setId(group.getId());
        mockMvc.perform(post("/admin/product/group/update").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSONObject.toJSONString(groupDto))).andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());

        ProductGroupDto product = new ProductGroupDto();
        product.setCurrentPage(1);
        product.setPageSize(10);
        product.setGroupId(group.getId());
        // stop4 查看商品分组的信息
        log.info("查询分组的商品");
        mockMvc.perform(post("/admin/product/group/findGroupProduct").contentType(MediaType.APPLICATION_JSON_UTF8).content(JSONObject.toJSONString(product))
        ).andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testFindNoExistProductGroup() throws Exception {
        Group group = this.groupService.findAll().get(0);
        ProductGroupDto productGroupDto = new ProductGroupDto();
        productGroupDto.setCurrentPage(1);
        productGroupDto.setPageSize(10);
        productGroupDto.setGroupId(group.getId());
        mockMvc.perform(post("/admin/product/group/findNoExistProductGroup").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSONObject.toJSONString(productGroupDto))).andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testDeleteProductGroupRef() throws Exception {
        ProductGroupDto productGroupDto = new ProductGroupDto();
        productGroupDto.setGroupId(1L);
        Set<String> ids = new HashSet<>();
        ids.add("1");
        productGroupDto.setShelfIds(ids);
        mockMvc.perform(post("/admin/product/group/deleteProductGroupRef").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSONObject.toJSONString(productGroupDto))).andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
        Condition condition = new Condition(ProductGroupRef.class);
        condition.createCriteria().andEqualTo("groupId", 1L);
        productGroupRefService.findByCondition(condition).stream().forEach(x -> System.out.println(JSONObject.toJSONString(x)));
    }

    @Test
    public void testUpdate() throws Exception {
        GroupCurdDto groupCurdDto = new GroupCurdDto();
        groupCurdDto.setId(1L);
        groupCurdDto.setTitle("空啊");
        groupCurdDto.setGroupType(Constants.GroupType.COMMON);
        Set<String> shelfIds = new HashSet<>();
        shelfIds.add("1");
        groupCurdDto.setShelfIds(shelfIds);
        mockMvc.perform(post("/admin/product/group/update").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSONObject.toJSONString(groupCurdDto))).andExpect(status().isOk());
        Condition condition = new Condition(ProductGroupRef.class);
        condition.createCriteria().andEqualTo("groupId", 1L);
        productGroupRefService.findByCondition(condition).stream().forEach(x -> System.out.println(JSONObject.toJSONString(x)));
    }


}
