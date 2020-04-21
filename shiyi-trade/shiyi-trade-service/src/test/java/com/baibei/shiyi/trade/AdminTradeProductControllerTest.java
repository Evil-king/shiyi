package com.baibei.shiyi.trade;

import com.alibaba.fastjson.JSONObject;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.trade.common.dto.TradeProductPicDto;
import com.baibei.shiyi.trade.common.vo.TradeProductSlideListVo;
import com.baibei.shiyi.trade.feign.bean.dto.TradeProductAddDto;
import com.baibei.shiyi.trade.feign.bean.dto.TradeProductDto;
import com.baibei.shiyi.trade.feign.bean.dto.TradeProductLookDto;
import com.baibei.shiyi.trade.service.IProductPicService;
import com.baibei.shiyi.trade.service.IProductService;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@WebAppConfiguration
public class AdminTradeProductControllerTest {

    private MockMvc mvc;
    @Autowired
    private WebApplicationContext context;
    @Before
    public void setupMockMvc() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }
    @Autowired
    private IProductService productService;
    @Autowired
    private IProductPicService productPicService;

    @Test
    public void listPage() throws Exception{
        TradeProductDto tradeProductDto = new TradeProductDto();
        tradeProductDto.setProductName("");
        tradeProductDto.setProductTradeNo("");
        tradeProductDto.setTradeStatus("");

        MvcResult result = mvc.perform(post("/shiyi/admin/trade/product/pageList")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSONObject.toJSONString(tradeProductDto))).andExpect(status().isOk()).andReturn();
        log.info("result={}",result.getResponse().getContentAsString());
    }

    @Test
    public void add() throws Exception{

        List<TradeProductAddDto.ImageDta> slideUrl = new ArrayList<>();
        List<TradeProductAddDto.ImageDta> detailsUrl = new ArrayList<>();
        List<TradeProductAddDto.ImageDta> iconUrl = new ArrayList<>();

        TradeProductAddDto.ImageDta imageDta = new TradeProductAddDto.ImageDta();
        imageDta.setUrl("http://test-ysl.zhongyiyoumei.cn/2020/01/06/7c3d84a5-19e0-4367-8b4c-d161b0914119.jpg");
        TradeProductAddDto.ImageDta imageDta1 = new TradeProductAddDto.ImageDta();
        imageDta1.setUrl("http://test-ysl.zhongyiyoumei.cn/2020/01/06/2d662380-965f-4ff5-9f3f-57aad1994ffe.jpg");
        slideUrl.add(imageDta);
        slideUrl.add(imageDta1);

        TradeProductAddDto.ImageDta detailsUrlDta = new TradeProductAddDto.ImageDta();
        detailsUrlDta.setUrl("http://test-ysl.zhongyiyoumei.cn/2020/01/06/bd0fa325-9fc7-4d1d-a5e1-3825f27586f5.jpg");
//        TradeProductAddDto.ImageDta detailsUrlDta1 = new TradeProductAddDto.ImageDta();
//        detailsUrlDta1.setUrl("222222222222222222222.png");
        detailsUrl.add(detailsUrlDta);
//        detailsUrl.add(detailsUrlDta1);


        TradeProductAddDto.ImageDta iconUrlDta = new TradeProductAddDto.ImageDta();
        iconUrlDta.setUrl("http://test-ysl.zhongyiyoumei.cn/2020/01/06/0a9601c8-f05b-452d-af3b-f865b075d241.jpg");
        iconUrl.add(iconUrlDta);

        TradeProductAddDto tradeProductAddDto = new TradeProductAddDto();
        tradeProductAddDto.setId(0L);
        tradeProductAddDto.setIcon(iconUrl);
        tradeProductAddDto.setCreator("xxxx");
        tradeProductAddDto.setModifier("xxxx111");
        tradeProductAddDto.setExchangePrice(new BigDecimal(100));
        tradeProductAddDto.setHighestQuotedPrice(new BigDecimal(50));
        tradeProductAddDto.setLowestQuotedPrice(new BigDecimal(10));
        tradeProductAddDto.setIssuePrice(new BigDecimal(500));
        tradeProductAddDto.setMarketTime("2019-12-24");
        tradeProductAddDto.setMaxTrade(5);
        tradeProductAddDto.setMinTrade(2);
        tradeProductAddDto.setProductTradeNo("test");
        tradeProductAddDto.setProductName("测试商品");
        tradeProductAddDto.setProductTradeNo("BN0002");
        tradeProductAddDto.setUnit("克");
        tradeProductAddDto.setSlideUrl(slideUrl);
        tradeProductAddDto.setDetailsUrl(detailsUrl);
        MvcResult result = mvc.perform(post("/shiyi/admin/trade/product/add")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSONObject.toJSONString(tradeProductAddDto))).andExpect(status().isOk()).andReturn();
        log.info("mvcResult={}", result);
    }

    @Test
    public void editOperator() throws Exception{
        List<TradeProductAddDto.ImageDta> slideUrl = new ArrayList<>();
        List<TradeProductAddDto.ImageDta> detailsUrl = new ArrayList<>();
        List<TradeProductAddDto.ImageDta> iconUrl = new ArrayList<>();

        TradeProductAddDto.ImageDta imageDta = new TradeProductAddDto.ImageDta();
        imageDta.setUrl("wwwwww.png");
        TradeProductAddDto.ImageDta imageDta1 = new TradeProductAddDto.ImageDta();
        imageDta1.setUrl("rrrrrrr.png");
        slideUrl.add(imageDta);
        slideUrl.add(imageDta1);

        TradeProductAddDto.ImageDta detailsUrlDta = new TradeProductAddDto.ImageDta();
        detailsUrlDta.setUrl("sdsdsdsdsd.png");
        TradeProductAddDto.ImageDta detailsUrlDta1 = new TradeProductAddDto.ImageDta();
        detailsUrlDta1.setUrl("qwqwqwqw.png");
        detailsUrl.add(detailsUrlDta);
        detailsUrl.add(detailsUrlDta1);


        TradeProductAddDto.ImageDta iconUrlDta = new TradeProductAddDto.ImageDta();
        iconUrlDta.setUrl("TTTTTT.icon");
        iconUrl.add(iconUrlDta);

        TradeProductAddDto tradeProductAddDto = new TradeProductAddDto();
        tradeProductAddDto.setId(1214424411350544386L);
        tradeProductAddDto.setIcon(iconUrl);
        tradeProductAddDto.setCreator("yyy");
        tradeProductAddDto.setModifier("sda23343");
        tradeProductAddDto.setExchangePrice(new BigDecimal(100));
        tradeProductAddDto.setHighestQuotedPrice(new BigDecimal(50));
        tradeProductAddDto.setLowestQuotedPrice(new BigDecimal(10));
        tradeProductAddDto.setIssuePrice(new BigDecimal(500));
        tradeProductAddDto.setMarketTime("2019-12-25");
        tradeProductAddDto.setMaxTrade(5);
        tradeProductAddDto.setMinTrade(2);
        tradeProductAddDto.setProductName("测试商品");
        tradeProductAddDto.setProductTradeNo("BN0002");
        tradeProductAddDto.setUnit("件");
        tradeProductAddDto.setSlideUrl(slideUrl);
        tradeProductAddDto.setDetailsUrl(detailsUrl);

        MvcResult result = mvc.perform(post("/shiyi/admin/trade/product/editOperator")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSONObject.toJSONString(tradeProductAddDto))).andExpect(status().isOk()).andReturn();
        log.info("mvcResult={}", result.getResponse().getContentAsString());
    }

    @Test
    public void look() throws Exception{
        TradeProductLookDto tradeProductEditDto = new TradeProductLookDto();
        tradeProductEditDto.setId(1214424411350544386L);
        MvcResult result = mvc.perform(post("/shiyi/admin/trade/product/look")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSONObject.toJSONString(tradeProductEditDto))).andExpect(status().isOk()).andReturn();
        log.info("mvcResult={}",result.getResponse().getContentAsString());
    }

    @Test
    public void modifyStatus() throws Exception{
        productService.modifyStatus();
    }

    @Test
    public void getTradeProductPic() throws Exception{
        TradeProductPicDto productPicDto = new TradeProductPicDto();
        productPicDto.setType("details");
        productPicDto.setProductTradeNo("zymh669");
        ApiResult<TradeProductSlideListVo> tradeProductPic = productPicService.getTradeProductPic(productPicDto);
        log.info("tradeProductPic={}",JSONObject.toJSONString(tradeProductPic));
    }
}