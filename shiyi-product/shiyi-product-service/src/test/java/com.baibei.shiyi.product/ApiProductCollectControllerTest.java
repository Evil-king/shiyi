package com.baibei.shiyi.product;

import com.alibaba.fastjson.JSONObject;
import com.baibei.shiyi.common.tool.bean.CustomerBaseAndPageDto;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.product.feign.bean.dto.ProductCollectDto;
import com.baibei.shiyi.product.feign.bean.dto.ProductGroupDto;
import com.baibei.shiyi.product.feign.bean.vo.GroupProductVo;
import com.baibei.shiyi.product.service.IProductCollectService;
import com.baibei.shiyi.product.service.IProductShelfService;
import com.baibei.shiyi.product.service.IProductStockService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;


import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ProductServiceApplication.class)
@Transactional
public class ApiProductCollectControllerTest {

    protected MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext wac;

    @Autowired
    private IProductShelfService productShelfService;

    @Autowired
    private IProductStockService productStockService;

    @Autowired
    private IProductCollectService productCollectService;

    @Before()  //这个方法在每个方法执行之前都会执行一遍
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();  //初始化MockMvc对象
    }

    @Test
    public void testPageList() throws Exception {
        CustomerBaseAndPageDto customerBaseAndPageDto = new CustomerBaseAndPageDto();
        customerBaseAndPageDto.setCurrentPage(1);
        customerBaseAndPageDto.setPageSize(20);
        customerBaseAndPageDto.setCustomerNo("1000539928");
        mockMvc.perform(post("/auth/api/product/collect/pageList")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(JSONObject.toJSONString(customerBaseAndPageDto))).andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    /**
     * 收藏商品
     *
     * @throws Exception
     */
    @Test
    public void testSave() throws Exception {
        ProductCollectDto productCollectDto = new ProductCollectDto();
        productCollectDto.setCustomerNo("5000000126");
        Set<String> shelfIds = new HashSet<>();
        shelfIds.add("1");
        shelfIds.add("2");
        productCollectDto.setShelfIds(shelfIds);
        mockMvc.perform(post("/auth/api/product/collect/save")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(JSONObject.toJSONString(productCollectDto))).andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
        CustomerBaseAndPageDto customerBaseAndPageDto = new CustomerBaseAndPageDto();
        customerBaseAndPageDto.setCurrentPage(1);
        customerBaseAndPageDto.setPageSize(10);
        productCollectService.pageList(customerBaseAndPageDto).getList().stream().forEach(
                result-> System.out.println(JSONObject.toJSONString(result))
        );
    }

    /**
     * 获取分组列表
     */
    @Test
    public void testFindNoExistProductGroup() {
        ProductGroupDto productGroupDto = new ProductGroupDto();
        productGroupDto.setCurrentPage(1);
        productGroupDto.setPageSize(10);
        productGroupDto.setGroupId(1L);
        MyPageInfo<GroupProductVo> pageInfo = productShelfService.findNoExistProductGroup(productGroupDto);
        System.out.println(JSONObject.toJSONString(pageInfo));

    }

    @Test
    public void batchDelete() throws Exception {
        ProductCollectDto productCollectDto = new ProductCollectDto();
        productCollectDto.setCustomerNo("5000000126");
        Set<String> shelfIds = new HashSet<>();
        shelfIds.add("2");
        mockMvc.perform(post("/auth/api/product/collect/deleteByIds")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(JSONObject.toJSONString(productCollectDto))).andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
        CustomerBaseAndPageDto customerBaseAndPageDto = new CustomerBaseAndPageDto();
        customerBaseAndPageDto.setCurrentPage(1);
        customerBaseAndPageDto.setPageSize(10);
        productCollectService.pageList(customerBaseAndPageDto).getList().stream().forEach(
                result-> System.out.println(JSONObject.toJSONString(result))
        );
    }


    @Test
    public void testDetuchStock() {
        /*DetuchStockDto detuchStockDto = new DetuchStockDto();
        detuchStockDto.setShelfId(1L);
        detuchStockDto.setSkuId(1L);
        detuchStockDto.setDetuchNum(new BigDecimal(10));
        detuchStockDto.setOrderNo("1111111111");
        detuchStockDto.setOperatorNo("0000001");
        productStockService.detuchStock(detuchStockDto);*/
    }

}
