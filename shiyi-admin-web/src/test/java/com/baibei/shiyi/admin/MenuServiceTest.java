package com.baibei.shiyi.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baibei.component.rocketmq.core.util.RocketMQUtil;
import com.baibei.shiyi.admin.modules.system.bean.dto.MenuDto;
import com.baibei.shiyi.admin.modules.system.bean.dto.MenuPageDto;
import com.baibei.shiyi.admin.modules.system.service.IMenuService;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.utils.IdWorker;
import com.baibei.shiyi.common.tool.utils.RandomUtils;
import lombok.extern.slf4j.Slf4j;
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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AdminWebApplication.class)
public class MenuServiceTest {

    @Autowired
    private IMenuService menuService;

    protected MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext wac;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();  //初始化MockMvc对象
    }

    @Autowired
    private RocketMQUtil rocketMQUtil;


    @Test
    public void testSaveMenu() {
        // 目录
        MenuDto menuDto1 = new MenuDto();
        menuDto1.setId(IdWorker.getId());
        menuDto1.setMenuType(Constants.MenuType.DIRECTORY);
        menuDto1.setHidden(Constants.Flag.UNVALID);
        menuDto1.setName("系统配置");
        menuDto1.setPermission("ROLE_SYSTEM");
        menuDto1.setPid(0L);
        menuDto1.setSeq(1L);
        menuService.save(menuDto1);

        MenuDto menuDto = new MenuDto();
        menuDto.setId(IdWorker.getId());
        menuDto.setMenuType(Constants.MenuType.DIRECTORY);
        menuDto.setHidden(Constants.Flag.UNVALID);
        menuDto.setName("订单管理");
        menuDto.setPermission("ROLE_ORDER");
        menuDto.setPid(0L);
        menuDto.setSeq(2L);
        menuService.save(menuDto);

        /**
         * 菜单
         */
        MenuDto menuDto2 = new MenuDto();
        menuDto2.setId(IdWorker.getId());
        menuDto2.setMenuType(Constants.MenuType.MENU);
        menuDto2.setHidden(Constants.Flag.UNVALID);
        menuDto2.setName("订单列表");
        menuDto2.setPermission("ROLE_ORDER");
        menuDto2.setPid(menuDto.getId());
        menuDto2.setSeq(3L);
        menuService.save(menuDto2);


        MenuDto menuDto3 = new MenuDto();
        menuDto3.setId(IdWorker.getId());
        menuDto3.setMenuType(Constants.MenuType.MENU);
        menuDto3.setHidden(Constants.Flag.UNVALID);
        menuDto3.setName("组织机构管理");
        menuDto3.setPermission("ROLE_OFFICE");
        menuDto3.setPid(menuDto1.getId());
        menuDto3.setSeq(4L);
        menuService.save(menuDto3);


        /**
         * 按钮
         */
        MenuDto menuDto4 = new MenuDto();
        menuDto4.setId(IdWorker.getId());
        menuDto4.setMenuType(Constants.MenuType.BUTTON);
        menuDto4.setHidden(Constants.Flag.UNVALID);
        menuDto4.setName("新建");
        menuDto4.setPermission("ROLE_ORDER_CREATE");
        menuDto4.setPid(menuDto2.getId());
        menuDto4.setSeq(5L);
        menuService.save(menuDto4);


        MenuDto menuDto5 = new MenuDto();
        menuDto5.setId(IdWorker.getId());
        menuDto5.setMenuType(Constants.MenuType.BUTTON);
        menuDto5.setHidden(Constants.Flag.UNVALID);
        menuDto5.setName("删除");
        menuDto5.setPermission("ROLE_OFFICE_CREATE");
        menuDto5.setPid(menuDto3.getId());
        menuDto5.setSeq(6L);
        menuService.save(menuDto5);
    }

    @Test
    public void testPageList() {
        MenuPageDto menuDto = new MenuPageDto();
        menuDto.setCurrentPage(1);
        menuDto.setPageSize(10);
        System.out.println(JSONObject.toJSONString(menuService.pageInfo(menuDto)));
    }

    @Test
    public void testGetAllMenu() throws Exception {
        mockMvc.perform(post("/admin/menu/getAllMenu").contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testSendMsg() {
        Map<String, Object> result = new HashMap<>();
        result.put("serialNo", "J0000000076116"); //银行流水号
        result.put("exchangeFundAccount", "5052480052"); //交易所资金账号
        result.put("occurAmount", "19800");
        result.put("bisinType", "1");
        result.put("exchangeId","1001022");
        result.put("bankAccount","6225882012585151");
        result.put("idKind", "111");
        result.put("idNo", "640221197911012111");
        result.put("initDate", "20191209");
        result.put("fuqingTransferReq", "{\"memberMainType\":\"2\",\"fullName\":\"���\",\"idKind\":\"111\",\"idNo\":\"640221197911012111\",\"initDate\":20191209,\"exchangeId\":\"1001022\",\"serialNo\":\"J0000000076116\",\"exchangeFundAccount\":\"5052480052\",\"fundPassword\":\" \",\"bankPassword\":\" \",\"moneyType\":\"CNY\",\"businType\":\"1\",\"bankProCode\":\"citicyq\",\"accountName\":\"���\",\"bankAccount\":\"6225882012585151\",\"occurAmount\":\"19800\",\"remark\":\"\",\"bisinType\":\"1\",\"allNotBlank\":false}");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
        StringBuilder str = new StringBuilder();
        str.append("D");
        str.append(formatter.format(LocalDate.now()));
        str.append(RandomUtils.getRandom(0, 10000));
        rocketMQUtil.sendMsg("Topic_fuqingDeposit", JSON.toJSONString(result), str.toString());
    }
}
