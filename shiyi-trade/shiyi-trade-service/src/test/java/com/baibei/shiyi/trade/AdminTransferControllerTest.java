package com.baibei.shiyi.trade;

import com.alibaba.fastjson.JSONObject;
import com.baibei.shiyi.trade.feign.bean.dto.TransferDataDto;
import com.baibei.shiyi.trade.feign.bean.dto.TransferLogDto;
import com.baibei.shiyi.trade.feign.bean.dto.TransferPageListDto;
import com.baibei.shiyi.trade.feign.bean.vo.TransferTemplateVo;
import com.google.common.collect.Lists;
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

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@WebAppConfiguration
public class AdminTransferControllerTest {

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    @Before
    public void setupMockMvc() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void pageListLog() throws Exception{

        TransferLogDto transferLogDto = new TransferLogDto();
        transferLogDto.setStartTime("");
        transferLogDto.setEndTime("");
        transferLogDto.setName("");
        transferLogDto.setStatus("");

        transferLogDto.setCurrentPage(1);
        transferLogDto.setPageSize(10);

        MvcResult result = mockMvc.perform(post("/shiyi/admin/trade/transfer/pageListLog")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSONObject.toJSONString(transferLogDto))).andExpect(status().isOk()).andReturn();

        log.info(result.getResponse().getContentAsString());
    }

    @Test
    public void listPage() throws Exception{
        TransferPageListDto transferPageListDto = new TransferPageListDto();
        transferPageListDto.setTransferLogId(1214826719685451778L);
        transferPageListDto.setInCustomerNo("");
        transferPageListDto.setOutCustomerNo("");
        transferPageListDto.setSerialNumber("");
        transferPageListDto.setStatus("");
        transferPageListDto.setType("wait");
        transferPageListDto.setCurrentPage(1);
        transferPageListDto.setPageSize(10);

        MvcResult result = mockMvc.perform(post("/shiyi/admin/trade/transfer/listPage")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSONObject.toJSONString(transferPageListDto))).andExpect(status().isOk()).andReturn();

        log.info(result.getResponse().getContentAsString());
    }


    @Test
    public void importData() throws Exception{
        List<TransferTemplateVo> transferTemplateListVo = Lists.newArrayList();
        TransferTemplateVo transferTemplateVo = new TransferTemplateVo();
        transferTemplateVo.setCostPrice("10");
        transferTemplateVo.setPrice("900");
        transferTemplateVo.setInCustomerNo("5052480052");
        transferTemplateVo.setNum("10");
        transferTemplateVo.setOutCustomerNo("027142130531");
        transferTemplateVo.setProductTradeNo("BN0002");
        transferTemplateVo.setRemark("测试");
        transferTemplateVo.setTradeTime("2020-01-11 00:00:00");
        transferTemplateVo.setCreator("hwq");
        transferTemplateVo.setModifier("hwq");
        transferTemplateVo.setIsFee("1");


        TransferTemplateVo transferTemplateVo1 = new TransferTemplateVo();
        transferTemplateVo.setCostPrice("10");
        transferTemplateVo.setPrice("900");
        transferTemplateVo1.setInCustomerNo("027142130531");
        transferTemplateVo.setNum("10");
        transferTemplateVo1.setOutCustomerNo("5052480052");
        transferTemplateVo1.setProductTradeNo("BN0002");
        transferTemplateVo1.setRemark("测试1");
        transferTemplateVo1.setTradeTime("2020-01-11 00:00:00");
        transferTemplateVo1.setCreator("rrr");
        transferTemplateVo1.setModifier("ssss");
        transferTemplateVo1.setIsFee("1");

        transferTemplateListVo.add(transferTemplateVo);
        transferTemplateListVo.add(transferTemplateVo1);

        MvcResult result = mockMvc.perform(post("/shiyi/admin/trade/transfer/importData")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSONObject.toJSONString(transferTemplateListVo))).andExpect(status().isOk()).andReturn();

        log.info(result.getResponse().getContentAsString());
    }



    @Test
    public void export() throws Exception{
        TransferPageListDto transferPageListDto = new TransferPageListDto();
        transferPageListDto.setInCustomerNo("");
        transferPageListDto.setOutCustomerNo("");
        transferPageListDto.setSerialNumber("");
        transferPageListDto.setStatus("");

        MvcResult result = mockMvc.perform(post("/shiyi/admin/trade/transfer/export")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSONObject.toJSONString(transferPageListDto))).andExpect(status().isOk()).andReturn();

        log.info(result.getResponse().getContentAsString());
    }

    @Test
    public void transferData() throws Exception{
        TransferDataDto transferDataDto = new TransferDataDto();
        transferDataDto.setTransferLogId(1217684936533618690L);

        MvcResult result = mockMvc.perform(post("/shiyi/admin/trade/transfer/transferData")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSONObject.toJSONString(transferDataDto))).andExpect(status().isOk()).andReturn();

        log.info(result.getResponse().getContentAsString());
    }
}