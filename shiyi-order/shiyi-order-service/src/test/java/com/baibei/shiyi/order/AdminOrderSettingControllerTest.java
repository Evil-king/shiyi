package com.baibei.shiyi.order;


import com.alibaba.fastjson.JSONObject;
import com.baibei.shiyi.order.feign.base.dto.AdminOrderSettingDto;
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
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = OrderServiceApplication.class)
public class AdminOrderSettingControllerTest {


    protected MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext wac;


    @Before()  //这个方法在每个方法执行之前都会执行一遍
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();  //初始化MockMvc对象
    }

    @Test
    public void testFindBySetting() throws Exception {
        mockMvc.perform(post("/admin/orderSetting/findBySetting")
                .contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testSetting() throws Exception {
        AdminOrderSettingDto orderSettingDto = new AdminOrderSettingDto();
        orderSettingDto.setConfirmOvertime(15);
        orderSettingDto.setFinishOvertime(15);
        orderSettingDto.setFlashOrderOvertime(30);
        orderSettingDto.setNormalOrderOvertime(30);
        mockMvc.perform(post("/admin/orderSetting/setting")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(JSONObject.toJSONString(orderSettingDto))).andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

}
