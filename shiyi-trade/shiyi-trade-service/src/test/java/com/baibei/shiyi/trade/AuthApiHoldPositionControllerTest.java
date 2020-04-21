package com.baibei.shiyi.trade;

import com.alibaba.fastjson.JSONObject;
import com.baibei.shiyi.account.feign.bean.dto.CustomerNoPageDto;
import com.baibei.shiyi.common.tool.bean.CustomerBaseDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@WebAppConfiguration
public class AuthApiHoldPositionControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setupMockMvc() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void getTotalMarketValue() throws Exception{
        CustomerBaseDto customerBaseDto = new CustomerBaseDto();
        customerBaseDto.setCustomerNo("5057175361");
//        customerBaseDto.setCustomerNo("1000539928");
        MvcResult result = mockMvc.perform(post("/auth/api/trade/holdPosition/getTotalMarketValue")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSONObject.toJSONString(customerBaseDto))).andExpect(status().isOk()).andReturn();

        log.info(result.getResponse().getContentAsString());
    }

    @Test
    public void getPageList() throws Exception{
        CustomerNoPageDto customerNoDto = new CustomerNoPageDto();
        customerNoDto.setCustomerNo("5057175361");
//        customerNoDto.setCustomerNo("1000539928");
        customerNoDto.setCurrentPage(1);
        customerNoDto.setPageSize(10);
        MvcResult result = mockMvc.perform(post("/auth/api/trade/holdPosition/getPageList")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSONObject.toJSONString(customerNoDto))).andExpect(status().isOk()).andReturn();

        log.info(result.getResponse().getContentAsString());
    }
}