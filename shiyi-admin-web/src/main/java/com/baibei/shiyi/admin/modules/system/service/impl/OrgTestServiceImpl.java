package com.baibei.shiyi.admin.modules.system.service.impl;

import com.baibei.shiyi.admin.modules.system.dao.OrgTestMapper;
import com.baibei.shiyi.admin.modules.system.dao.OrganizationInfomationMapper;
import com.baibei.shiyi.admin.modules.system.dao.OrganizationMapper;
import com.baibei.shiyi.admin.modules.system.model.AgentTest;
import com.baibei.shiyi.admin.modules.system.model.OrgTest;
import com.baibei.shiyi.admin.modules.system.model.Organization;
import com.baibei.shiyi.admin.modules.system.model.OrganizationInfomation;
import com.baibei.shiyi.admin.modules.system.service.IOrgTestService;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.utils.IdWorker;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;


/**
* @author: hyc
* @date: 2019/11/28 15:57:22
* @description: OrgTest服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class OrgTestServiceImpl extends AbstractService<OrgTest> implements IOrgTestService {

    @Autowired
    private OrgTestMapper tblOrgTestMapper;
    @Autowired
    private OrganizationMapper organizationMapper;

    @Autowired
    private OrganizationInfomationMapper infomationMapper;
    @Override
    public void createAgent() {
        Condition condition=new Condition(OrgTest.class);
        Example.Criteria criteria=condition.createCriteria();
        criteria.andEqualTo("flag","1");
        List<OrgTest> agentTests=tblOrgTestMapper.selectByCondition(condition);
        for (int i = 0; i <agentTests.size() ; i++) {
            OrgTest agentTest = agentTests.get(i);
            Organization organization=new Organization();
            organization.setId(IdWorker.getId());
            organization.setOrgCode(agentTest.getOrgCode()+"");
            organization.setOrgName(agentTest.getOrgName());
            //暂时将pid改成手机号
            organization.setPid(StringUtils.isEmpty(agentTest.getCauseMobile()) ?0:Long.valueOf(agentTest.getCauseMobile()));
            if(!StringUtils.isEmpty(agentTest.getCauseMobile())){
                //如果所属事业部手机号为空，那么他就是事业部
                organization.setOrgType(Constants.OrganizationType.ORGANIZATION);
            }else{
                organization.setOrgType(Constants.OrganizationType.BUSINESS);
            }
            organization.setCreateTime(new Date());
            organization.setModifyTime(new Date());
            organization.setFlag(new Byte("1"));
            organization.setCreateBy("admin");
            organization.setOrgStatus(Constants.OrganizationStatus.ENABLE);
            organization.setRegisterMobile(agentTest.getOrgMobile());
            //暂时将用户编号改成手机号
            organization.setCustomerNO(agentTest.getOrgMobile());
            organizationMapper.insertSelective(organization);
            OrganizationInfomation infomation=new OrganizationInfomation();
            infomation.setId(IdWorker.getId());
            infomation.setBusinessType("1".equals(agentTest.getBusinessType())?Constants.BusinessType.PERSONAL:Constants.BusinessType.ENTERPRISE);
            infomation.setOrganizationId(organization.getId());
            //将返还账号改成注册代理手机号
            infomation.setReturnAccount(agentTest.getOrgMobile());
            infomationMapper.insertSelective(infomation);
            agentTest.setFlag(0L);
            agentTest.setUpdateTime(new Date());
            tblOrgTestMapper.updateByPrimaryKeySelective(agentTest);
        }
    }
}
