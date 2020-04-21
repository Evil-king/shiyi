package com.baibei.shiyi.trade;

import com.alibaba.fastjson.JSONObject;
import com.baibei.shiyi.trade.common.dto.DealOrderQueryDTO;
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
public class AuthHistoryTradeOrderTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setupMockMvc() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void customerHistoryList() throws Exception{
        DealOrderQueryDTO dealOrderQueryDTO = new DealOrderQueryDTO();
        dealOrderQueryDTO.setCustomerNo("027142130531");
        dealOrderQueryDTO.setCurrentPage(1);
        dealOrderQueryDTO.setPageSize(10);
        MvcResult result = mockMvc.perform(post("/auth/api/trade/historyTrade/customerHistoryList")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSONObject.toJSONString(dealOrderQueryDTO))).andExpect(status().isOk()).andReturn();

        log.info(result.getResponse().getContentAsString());
    }
}