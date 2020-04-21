package com.baibei.shiyi.trade;

import com.baibei.shiyi.trade.common.dto.PickOrderDto;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@WebAppConfiguration
public class AuthApiPickOrderTest {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;


    @Before
    public void setupMockMvc() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }


    /**
     * 测试提货订单申请
     */
    @Test
    public void testApplyDelivery() throws Exception {

        // stop 1 传产品信息
        PickOrderDto pickOrderDto = new PickOrderDto();
        pickOrderDto.setAddressId(32L);
        pickOrderDto.setPickNumber(1);
        pickOrderDto.setSpuNo("NB001");
        pickOrderDto.setCustomerId(3);
        pickOrderDto.setCustomerNo("13711449453");


        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(pickOrderDto);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/auth/api/pickOrder/applyDelivery")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(requestJson).accept(MediaType.APPLICATION_JSON_UTF8)).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        System.out.println(content);
    }


}
