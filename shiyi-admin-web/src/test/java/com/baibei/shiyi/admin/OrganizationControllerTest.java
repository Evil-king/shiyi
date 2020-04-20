package com.baibei.shiyi.admin;

import com.alibaba.fastjson.JSONObject;

import com.baibei.shiyi.admin.modules.system.bean.dto.OrganizationBusinessDto;
import com.baibei.shiyi.admin.modules.system.model.Organization;
import com.baibei.shiyi.admin.modules.system.model.OrganizationInfomation;
import com.baibei.shiyi.admin.modules.system.service.IOrganizationInfomationService;
import com.baibei.shiyi.admin.modules.system.service.IOrganizationService;

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

import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AdminWebApplication.class)
public class OrganizationControllerTest {

    protected MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext wac;

    @Autowired
    private IOrganizationService organizationService;

    @Autowired
    private IOrganizationInfomationService organizationInfomationService;


    @Before
    public void init() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();  //初始化MockMvc对象
    }


    /**
     * 保存事业部
     */
    @Test
    public void testSaveBusiness() throws Exception {
        OrganizationBusinessDto organizationBusinessDto = new OrganizationBusinessDto();
        organizationBusinessDto.setOrgCode("T1001");
        organizationBusinessDto.setOrgName("妹妹公司");
        organizationBusinessDto.setBusinessType(Constants.BusinessType.PERSONAL);
        organizationBusinessDto.setName("张三");
        organizationBusinessDto.setIdCard("123456789012345678");
        organizationBusinessDto.setReturnAccount("1234567489");
        organizationBusinessDto.setOrgType(Constants.OrganizationType.BUSINESS);
        organizationBusinessDto.setRebateFreezeRatio(new Float(0.36));


        OrganizationBusinessDto organizationBusinessDto2 = new OrganizationBusinessDto();
        organizationBusinessDto2.setOrgCode("T1002");
        organizationBusinessDto2.setOrgName("哥哥公司");
        organizationBusinessDto2.setBusinessType(Constants.BusinessType.ENTERPRISE);
        organizationBusinessDto2.setCompanyName("好人公司");
        organizationBusinessDto2.setBusinessLicense("456456789789789798798789");
        organizationBusinessDto2.setOrganizationCodeCertificate("1234567999999"); //组织机构代码证
        organizationBusinessDto2.setTaxRegistrationCertificate("123456897897897897987"); // 税务登记证
        organizationBusinessDto2.setOrgType(Constants.OrganizationType.BUSINESS);
        organizationBusinessDto2.setReturnAccount("1234567489");
        organizationBusinessDto2.setRebateFreezeRatio(new Float(0.36));
        mockMvc.perform(post("/admin/organization/business/save")

                .contentType(MediaType.APPLICATION_JSON_UTF8).content(JSONObject.toJSONString(organizationBusinessDto))).andExpect(status().isOk());

        mockMvc.perform(post("/admin/organization/business/save")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(JSONObject.toJSONString(organizationBusinessDto2))).andExpect(status().isOk());
    }


    @Test
    public void testUpdateBusiness() throws Exception {
        List<Organization> organizationList = organizationService.findByOrgType(Constants.OrganizationType.BUSINESS);
        Assert.assertEquals(organizationList.isEmpty(),false );
        Organization organization = organizationList.get((int) (Math.random() * organizationList.size()));

        OrganizationInfomation organizationInfomation = organizationInfomationService.findByOrganizationId(organization.getId());
        Assert.assertNotNull(organizationInfomation);
        OrganizationBusinessDto organizationBusinessDto = new OrganizationBusinessDto();
        organizationBusinessDto.setId(organization.getId());
        organizationBusinessDto.setOrgCode(organization.getOrgCode());
        organizationBusinessDto.setOrgName(organization.getOrgName());
        organizationBusinessDto.setBusinessType(organizationInfomation.getBusinessType());
        organizationBusinessDto.setIdCard(organizationBusinessDto.getIdCard());
        organizationBusinessDto.setName("销售事业部");

        mockMvc.perform(post("/admin/organization/business/update")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(JSONObject.toJSONString(organizationBusinessDto))).andExpect(status().isOk()).andDo(print());

    }


    /**
     * 保存机构
     *
     * @throws Exception
     */
    @Test
    public void testSaveAgency() throws Exception {
        List<Organization> organizationList = organizationService.findByOrgType(Constants.OrganizationType.BUSINESS);
        Assert.assertTrue((!organizationList.isEmpty()));
        OrganizationBusinessDto organizationBusinessDto = new OrganizationBusinessDto();
        organizationBusinessDto.setOrgCode("C3005");
        organizationBusinessDto.setOrgName("美琪公司");
        organizationBusinessDto.setBusinessType(Constants.BusinessType.ENTERPRISE);
        organizationBusinessDto.setCompanyName("比卡丘公司");
        organizationBusinessDto.setBusinessLicense("456456789789789798798789");
        organizationBusinessDto.setOrganizationCodeCertificate("1234567999999"); //组织机构代码证
        organizationBusinessDto.setTaxRegistrationCertificate("123456897897897897987"); // 税务登记证
        organizationBusinessDto.setOrgType(Constants.OrganizationType.BUSINESS);
        organizationBusinessDto.setReturnAccount("1234567489");
        organizationBusinessDto.setPid(organizationList.get(0).getId());
        mockMvc.perform(post("/admin/organization/agency/save")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(JSONObject.toJSONString(organizationBusinessDto))).andExpect(status().isOk());
    }

    /**
     * 市代理保存
     */
    @Test
    public void testSaveCityProxy() throws Exception {
        List<Organization> organizationList = organizationService.findByOrgType(Constants.OrganizationType.ORGANIZATION);
        Assert.assertTrue((!organizationList.isEmpty()));
        OrganizationBusinessDto organizationBusinessDto = new OrganizationBusinessDto();
        organizationBusinessDto.setOrgType(Constants.OrganizationType.CITYAGENT); //市代理
        organizationBusinessDto.setOrgName("广州市程序员大本营");
        organizationBusinessDto.setOrgCode("T8885992");
        organizationBusinessDto.setPid(organizationList.get((int) (Math.random() * organizationList.size())).getId()); //设置组织机构Id
        organizationBusinessDto.setBusinessType(Constants.BusinessType.ENTERPRISE); //企业
        organizationBusinessDto.setCompanyName("广州市分代理");
        organizationBusinessDto.setBusinessLicense("131415926"); // 工商营业证
        organizationBusinessDto.setOrganizationCodeCertificate("9396456677");
        organizationBusinessDto.setTaxRegistrationCertificate("8877897878921133");
        mockMvc.perform(post("/admin/organization/proxy/save")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(JSONObject.toJSONString(organizationBusinessDto))).andExpect(status().isOk());
    }

    /**
     * 区代理
     */
    @Test
    public void testSaveAreaProxy() throws Exception {
        List<Organization> organizationList = organizationService.findByOrgType(Constants.OrganizationType.CITYAGENT);
        OrganizationBusinessDto organizationBusinessDto = new OrganizationBusinessDto();
        organizationBusinessDto.setOrgType(Constants.OrganizationType.AREAAGENT); //市代理
        organizationBusinessDto.setOrgName("广州市程序员小本营");
        organizationBusinessDto.setOrgCode("T8885993");
        organizationBusinessDto.setPid(organizationList.get((int) (Math.random() * organizationList.size())).getId()); //设置组织机构Id
        organizationBusinessDto.setBusinessType(Constants.BusinessType.ENTERPRISE); //企业
        organizationBusinessDto.setCompanyName("广州市区代理");
        organizationBusinessDto.setBusinessLicense("131415926"); // 工商营业证
        organizationBusinessDto.setOrganizationCodeCertificate("9396456677");
        organizationBusinessDto.setTaxRegistrationCertificate("8877897878921133");
        mockMvc.perform(post("/admin/organization/proxy/save")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(JSONObject.toJSONString(organizationBusinessDto))).andExpect(status().isOk());
    }

    /**
     * 保存普通代理
     */
    @Test
    public void testSaveProxy() throws Exception {
        List<Organization> organizationList = organizationService.findByOrgType(Constants.OrganizationType.AREAAGENT);
        OrganizationBusinessDto organizationBusinessDto = new OrganizationBusinessDto();
        organizationBusinessDto.setOrgType(Constants.OrganizationType.ORDINARYAGENT); //市代理
        organizationBusinessDto.setOrgName("广州市程序员小小本营");
        organizationBusinessDto.setOrgCode("T8885994");
        organizationBusinessDto.setPid(organizationList.get((int) (Math.random() * organizationList.size())).getId()); //设置组织机构Id
        organizationBusinessDto.setBusinessType(Constants.BusinessType.ENTERPRISE); //企业
        organizationBusinessDto.setCompanyName("广州市普通代理");
        organizationBusinessDto.setBusinessLicense("131415926"); // 工商营业证
        organizationBusinessDto.setOrganizationCodeCertificate("9396456677");
        organizationBusinessDto.setTaxRegistrationCertificate("8877897878921133");
        mockMvc.perform(post("/admin/organization/proxy/save")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(JSONObject.toJSONString(organizationBusinessDto))).andExpect(status().isOk());
    }


    /**
     * 保存分公司
     */
    @Test
    public void testBranchCompany() throws Exception {
        List<Organization> organizationList = organizationService.findByOrgType(Constants.OrganizationType.ORGANIZATION);
        Assert.assertTrue((!organizationList.isEmpty()));
        OrganizationBusinessDto organizationBusinessDto = new OrganizationBusinessDto();
        organizationBusinessDto.setPid(organizationList.get((int) (Math.random() * organizationList.size())).getId());
        organizationBusinessDto.setOrgName("清清分公司");
        organizationBusinessDto.setOrgCode("123456");
        organizationBusinessDto.setBusinessType(Constants.BusinessType.PERSONAL);
        organizationBusinessDto.setName("李四");
        organizationBusinessDto.setIdCard("123456789012345678");

        mockMvc.perform(post("/admin/organization/branch/save").
                contentType(MediaType.APPLICATION_JSON_UTF8).content(JSONObject.toJSONString(organizationBusinessDto))).andExpect(status().isOk()).andDo(print());
    }

}
