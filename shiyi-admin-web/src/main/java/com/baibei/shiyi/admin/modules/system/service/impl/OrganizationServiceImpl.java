package com.baibei.shiyi.admin.modules.system.service.impl;

import com.baibei.shiyi.admin.modules.security.utils.SecurityUtils;
import com.baibei.shiyi.admin.modules.system.bean.dto.OrganizationBusinessDto;
import com.baibei.shiyi.admin.modules.system.bean.dto.OrganizationBusinessPageDto;
import com.baibei.shiyi.admin.modules.system.bean.dto.OrganizationTypeDto;
import com.baibei.shiyi.admin.modules.system.bean.vo.*;
import com.baibei.shiyi.admin.modules.system.dao.OrganizationMapper;
import com.baibei.shiyi.admin.modules.system.model.BranchOfficeProxy;
import com.baibei.shiyi.admin.modules.system.model.Organization;
import com.baibei.shiyi.admin.modules.system.model.OrganizationInfomation;
import com.baibei.shiyi.admin.modules.system.service.IBranchOfficeProxyService;
import com.baibei.shiyi.admin.modules.system.service.IOrganizationInfomationService;
import com.baibei.shiyi.admin.modules.system.service.IOrganizationService;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.api.ResultEnum;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.exception.ServiceException;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.common.tool.utils.BeanUtil;
import com.baibei.shiyi.common.tool.utils.IdWorker;
import com.baibei.shiyi.user.feign.client.admin.AdminCustomerFeign;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.*;


/**
 * @author: hyc
 * @date: 2019/10/10 09:51:43
 * @description: Organization服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class OrganizationServiceImpl extends AbstractService<Organization> implements IOrganizationService {

    @Autowired
    private OrganizationMapper tblAdminOrganizationMapper;

    @Autowired
    private IOrganizationInfomationService organizationInfomationService;

    @Autowired
    private IBranchOfficeProxyService officeProxyService;

    @Autowired
    private AdminCustomerFeign adminCustomerFeign;

//
//    @Autowired
//    private SmsFeign smsFeign;


    @Override
    public MyPageInfo<OrganizationBusinessVo> businessPageList(OrganizationBusinessPageDto organizationBusinessDto) {
        MyPageInfo<OrganizationBusinessVo> myPageInfo = new MyPageInfo<>(businessList(organizationBusinessDto));
        return myPageInfo;
    }


    @Override
    public Long saveBusinessUnit(OrganizationBusinessDto organizationBusinessDto) {
        ApiResult apiResult = this.verification(organizationBusinessDto);
        if (!apiResult.getCode().equals(ResultEnum.SUCCESS.getCode())) {
            throw new ServiceException(apiResult.getMsg());
        }
        if (repeatVerificationOrgCode(organizationBusinessDto.getOrgCode(), null)) {
            String orgTypeName = getOrgType(organizationBusinessDto.getOrgType());
            throw new ServiceException(orgTypeName + "编号已经存在");
        }
        if (repeatVerificationOrgName(organizationBusinessDto.getOrgName(), null)) {
            String orgTypeName = getOrgType(organizationBusinessDto.getOrgType());
            throw new ServiceException(orgTypeName + "名称已经存在");
        }

        Organization organization = BeanUtil.copyProperties(organizationBusinessDto, Organization.class);
        organization.setId(IdWorker.getId());
        organization.setCreateTime(new Date());
        organization.setFlag(new Byte(Constants.Flag.VALID));
        organization.setOrgStatus(Constants.OrganizationStatus.ENABLE);
        organization.setModifyTime(new Date());
        organization.setCreateBy(SecurityUtils.getUsername());
        this.save(organization);

        OrganizationInfomation organizationInfomation = new OrganizationInfomation();
        organizationBusinessDto.setId(organization.getId());
        this.toEntity(organizationInfomation, organizationBusinessDto);
        organizationInfomation.setId(IdWorker.getId()); //设置组织信息Id
        organizationInfomationService.save(organizationInfomation);
        return organization.getId();
    }

    private String getOrgType(String orgType) {
        switch (orgType) {
            case Constants.OrganizationType.BUSINESS:
                return "事业部";
            case Constants.OrganizationType.ORGANIZATION:
                return "组织机构";
            case Constants.OrganizationType.BRANCHOFFICE:
                return "分公司";
            case Constants.OrganizationType.CITYAGENT:
                return "代理";
            case Constants.OrganizationType.AREAAGENT:
                return "代理";
            case Constants.OrganizationType.ORDINARYAGENT:
                return "代理";
            default:
                return "机构";
        }
    }

    @Override
    public void updateBusinessUnit(OrganizationBusinessDto organizationBusinessDto) {
        // stop1 修改组织机构信息
        ApiResult apiResult = this.verification(organizationBusinessDto);
        if (!apiResult.getCode().equals(ResultEnum.SUCCESS.getCode())) {
            throw new ServiceException(apiResult.getMsg());
        }
        if (repeatVerificationOrgCode(organizationBusinessDto.getOrgCode(), organizationBusinessDto.getId())) {
            String orgTypeName = getOrgType(organizationBusinessDto.getOrgType());
            throw new ServiceException(orgTypeName + "编号已经存在");
        }
        if (repeatVerificationOrgName(organizationBusinessDto.getOrgName(), organizationBusinessDto.getId())) {
            String orgTypeName = getOrgType(organizationBusinessDto.getOrgType());
            throw new ServiceException(orgTypeName + "名称已经存在");
        }

        Organization organization = BeanUtil.copyProperties(organizationBusinessDto, Organization.class); //这里不能用toSelect
        organization.setModifyTime(new Date());
        this.update(organization);

        OrganizationInfomation organizationInfomation = this.organizationInfomationService.findByOrganizationId(organization.getId());
        this.toEntity(organizationInfomation, organizationBusinessDto);
        organizationInfomationService.updateNull(organizationInfomation);
    }

    @Override
    public List<OrganizationBusinessVo> businessList(OrganizationBusinessPageDto organizationBusinessPageDto) {
        if (organizationBusinessPageDto.getCurrentPage() != null && organizationBusinessPageDto.getPageSize() != null) {
            PageHelper.startPage(organizationBusinessPageDto.getCurrentPage(), organizationBusinessPageDto.getPageSize());
        }
        List<OrganizationBusinessVo> list = tblAdminOrganizationMapper.businessPageList(organizationBusinessPageDto);
        return list;
    }

    @Override
    public MyPageInfo<OrganizationAgencyVo> agencyPageList(OrganizationBusinessPageDto organizationBusinessPageDto) {
        return new MyPageInfo<>(agencyList(organizationBusinessPageDto));
    }

    @Override
    public List<OrganizationAgencyVo> agencyList(OrganizationBusinessPageDto organizationBusinessPageDto) {
        if (organizationBusinessPageDto.getCurrentPage() != null && organizationBusinessPageDto.getPageSize() != null) {
            PageHelper.startPage(organizationBusinessPageDto.getCurrentPage(), organizationBusinessPageDto.getPageSize());
        }
        List<OrganizationAgencyVo> organizationAgencyVos = this.tblAdminOrganizationMapper.agencyPageList(organizationBusinessPageDto);
        return organizationAgencyVos;
    }

    @Override
    public OrganizationAgencyVo getByAgencyId(Long orgId) {
        return this.tblAdminOrganizationMapper.findByAgencyId(orgId);
    }

    @Override
    public List<Organization> findByPid(Long pid) {
        Condition condition = new Condition(Organization.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("pid", pid);
        criteria.andEqualTo("flag", new Byte(Constants.Flag.VALID));
        return this.findByOrgTypeAndPid(pid, null);
    }

    @Override
    public List<Organization> findByOrgTypeAndPid(Long pid, String orgType) {
        Condition condition = new Condition(Organization.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("pid", pid);
        if (orgType != null) {
            criteria.andEqualTo("orgType", orgType);
        }
        criteria.andEqualTo("flag", new Byte(Constants.Flag.VALID));
        return this.findByCondition(condition);
    }


    @Override
    public MyPageInfo<OrganizationBranchVo> pageListBranchCompany(OrganizationBusinessPageDto organizationBusinessPageDto) {
        if (organizationBusinessPageDto.getCurrentPage() != null && organizationBusinessPageDto.getPageSize() != null) {
            PageHelper.startPage(organizationBusinessPageDto.getCurrentPage(), organizationBusinessPageDto.getPageSize());
        }
        List<OrganizationBranchVo> organizationBusinessVos = this.tblAdminOrganizationMapper.findByBranchCompanyPageList(organizationBusinessPageDto);
        MyPageInfo<OrganizationBranchVo> pageInfo = new MyPageInfo<>(organizationBusinessVos);
        return pageInfo;
    }


    @Override
    public Boolean isHaveChildren(Long orgId) {
        List<Organization> organizationList = this.findByPid(orgId);
        if (organizationList.size() > 0) {
            return true;
        }
        return false;
    }

    @Override
    public void saveBranchCompany(OrganizationBusinessDto organizationBusinessDto) {
        Long branchCompanyId = saveBusinessUnit(organizationBusinessDto); //保存组织机构
        // 去重
        Set<Long> orgSetId = new HashSet<>();
        organizationBusinessDto.getBranchAssociateId().stream().forEach(org -> org.stream().forEach(orgId -> orgSetId.add(orgId)));
        // 添加到代理里面去
        orgSetId.stream().forEach(orgId -> saveBranchCompany(orgId, branchCompanyId));
    }


    @Override
    public void updateBranchCompany(OrganizationBusinessDto organizationBusinessDto) {
        updateBusinessUnit(organizationBusinessDto);
        // stop1 删除分公司代表的分公司信息
        officeProxyService.deleteByBranchCompanyId(organizationBusinessDto.getId());
        Set<Long> orgSetId = new HashSet<>();
        organizationBusinessDto.getBranchAssociateId().stream().forEach(org -> org.stream().forEach(orgId -> orgSetId.add(orgId)));
        orgSetId.stream().forEach(orgId -> saveBranchCompany(orgId, organizationBusinessDto.getId()));
    }

    /**
     * 保存分公司与组织机构关联的信息
     *
     * @param orgId
     * @param branchCompanyId
     */
    private void saveBranchCompany(Long orgId, Long branchCompanyId) {
        BranchOfficeProxy officeProxy = new BranchOfficeProxy();
        officeProxy.setId(IdWorker.getId());
        officeProxy.setBranchCompanyId(branchCompanyId);
        officeProxy.setOrganizationId(orgId);
        officeProxy.setCreateTime(new Date());
        officeProxy.setModifyTime(new Date());
        officeProxy.setCreateBy(SecurityUtils.getUsername());
        officeProxyService.save(officeProxy);
    }


    @Override
    public OrganizationBranchVo getBranchCompanyId(Long branchCompanyId) {
        OrganizationBranchVo organizationBranchVo = this.tblAdminOrganizationMapper.findByOneBranchCompanyId(branchCompanyId);

        // stop1 查询顶级节点
        List<Organization> branchCompanyList = this.tblAdminOrganizationMapper.findByBranchCompanyId(branchCompanyId, Constants.OrganizationType.CITYAGENT);

        // stop 根据代理Id和父Id查询结果
        List<List<Long>> branchAssociateIdList = new LinkedList();
        branchCompanyList.stream().forEach(result -> {
            List<Long> branchAssociateList = new ArrayList<>();
            branchAssociateList.add(result.getId());
            getBranchChildren(branchCompanyId, result.getId(), branchAssociateList, branchAssociateIdList);
        });
        organizationBranchVo.setBranchAssociateId(branchAssociateIdList);
        return organizationBranchVo;
    }

    private void getBranchChildren(Long branchCompanyId, Long pid, List<Long> childList, List<List<Long>> branchAssociateIdList) {
        List<Organization> branchCompanyList = this.tblAdminOrganizationMapper.findByBranchCompanyIdAndPid(branchCompanyId, pid);
        // stop1 没有子节点就结束
        if (branchCompanyList.isEmpty()) {
            branchAssociateIdList.add(childList);
            return;
        }
        // stop2 遍历所有的父节点
        for (Organization organization : branchCompanyList) {
            List<Long> fatherList = new ArrayList<>();
            fatherList.addAll(childList);
            fatherList.add(organization.getId());
            getBranchChildren(branchCompanyId, organization.getId(), fatherList, branchAssociateIdList);
        }
    }


    /**
     * 代理List
     *
     * @param organizationBusinessPageDto
     * @return
     */
    @Override
    public MyPageInfo<OrganizationProxyVo> pageListProxy(OrganizationBusinessPageDto organizationBusinessPageDto) {
        if (organizationBusinessPageDto.getCurrentPage() != null && organizationBusinessPageDto.getPageSize() != null) {
            PageHelper.startPage(organizationBusinessPageDto.getCurrentPage(), organizationBusinessPageDto.getPageSize());
        }
        List<OrganizationProxyVo> organizationBusinessVos = this.tblAdminOrganizationMapper.findByProxyList(organizationBusinessPageDto);
        organizationBusinessVos.stream().forEach(result -> getProxyAllFather(result, true));
        MyPageInfo<OrganizationProxyVo> pageInfo = new MyPageInfo(organizationBusinessVos);
        return pageInfo;
    }


    /**
     * excel 导出新改动
     *
     * @param organizationBusinessPageDto
     * @return
     */
    public MyPageInfo<OrganizationProxyVo> pageListExportProxy(OrganizationBusinessPageDto organizationBusinessPageDto) {
        if (organizationBusinessPageDto.getCurrentPage() != null && organizationBusinessPageDto.getPageSize() != null) {
            PageHelper.startPage(organizationBusinessPageDto.getCurrentPage(), organizationBusinessPageDto.getPageSize());
        }
        List<OrganizationProxyVo> organizationBusinessVos = this.tblAdminOrganizationMapper.findByProxyList(organizationBusinessPageDto);
        organizationBusinessVos.stream().forEach(result -> getProxyExportAllFather(result, true));
        MyPageInfo<OrganizationProxyVo> pageInfo = new MyPageInfo(organizationBusinessVos);
        return pageInfo;
    }


    /***
     *
     * @param organizationProxyVo
     * @param IsShowPid
     */
    private void getProxyExportAllFather(OrganizationProxyVo organizationProxyVo, Boolean IsShowPid) {
        Map<String, String> pidText = new HashMap<>();
        Map<String, Long> pidId = new HashMap<>();
        Map<String, String> orgCode = new HashMap<>();
        pidText.put(Constants.OrganizationType.ORGANIZATION, ""); // 机构
        pidText.put(Constants.OrganizationType.BUSINESS, ""); // 事业部
        pidText.put(Constants.OrganizationType.CITYAGENT, ""); // 市代理
        pidText.put(Constants.OrganizationType.AREAAGENT, ""); // 区代理
        this.findAllFather(String.valueOf(organizationProxyVo.getId())).forEach(father -> {
            if (!father.getId().equals(organizationProxyVo.getId())) {
                if (pidText.get(father.getOrgType()) != null) {
                    pidText.put(father.getOrgType(), father.getOrgName());
                    pidId.put(father.getOrgType(), father.getId());
                    orgCode.put(father.getOrgType(),father.getOrgCode());
                }
            }
        });
        // stop1 各种父类的名字
        organizationProxyVo.setAreaAgent(orgCode.get(Constants.OrganizationType.AREAAGENT));
        organizationProxyVo.setCityAgent(orgCode.get(Constants.OrganizationType.CITYAGENT));
        organizationProxyVo.setOrganizationName(pidText.get(Constants.OrganizationType.ORGANIZATION));
        organizationProxyVo.setBusinessName(pidText.get(Constants.OrganizationType.BUSINESS));


        // stop2 各种父类的Id
        if (IsShowPid) {
            organizationProxyVo.setOrganizationId(pidId.get(Constants.OrganizationType.ORGANIZATION));
            organizationProxyVo.setCityAgentId(pidId.get(Constants.OrganizationType.CITYAGENT));
            organizationProxyVo.setAreaAgentId(pidId.get(Constants.OrganizationType.AREAAGENT));
        }
        List<String> areaList = new ArrayList<>();
        areaList.add(organizationProxyVo.getAgentAreaProvince()); // 获取省
        areaList.add(organizationProxyVo.getAgentAreaCity()); // 获取市
        areaList.add(organizationProxyVo.getAgentAreaRegion()); //获取区
        organizationProxyVo.setAgentAreaList(areaList); //省市区凭借
    }


    private void getProxyAllFather(OrganizationProxyVo organizationProxyVo, Boolean IsShowPid) {
        Map<String, String> pidText = new HashMap<>();
        Map<String, Long> pidId = new HashMap<>();
        pidText.put(Constants.OrganizationType.ORGANIZATION, ""); // 机构
        pidText.put(Constants.OrganizationType.BUSINESS, ""); // 事业部
        pidText.put(Constants.OrganizationType.CITYAGENT, ""); // 市代理
        pidText.put(Constants.OrganizationType.AREAAGENT, ""); // 区代理
        this.findAllFather(String.valueOf(organizationProxyVo.getId())).forEach(father -> {
            if (!father.getId().equals(organizationProxyVo.getId())) {
                if (pidText.get(father.getOrgType()) != null) {
                    pidText.put(father.getOrgType(), father.getOrgName());
                    pidId.put(father.getOrgType(), father.getId());
                }
            }
        });
        // stop1 各种父类的名字
        organizationProxyVo.setAreaAgent(pidText.get(Constants.OrganizationType.AREAAGENT));
        organizationProxyVo.setCityAgent(pidText.get(Constants.OrganizationType.CITYAGENT));
        organizationProxyVo.setOrganizationName(pidText.get(Constants.OrganizationType.ORGANIZATION));
        organizationProxyVo.setBusinessName(pidText.get(Constants.OrganizationType.BUSINESS));
        // stop2 各种父类的Id
        if (IsShowPid) {
            organizationProxyVo.setOrganizationId(pidId.get(Constants.OrganizationType.ORGANIZATION));
            organizationProxyVo.setCityAgentId(pidId.get(Constants.OrganizationType.CITYAGENT));
            organizationProxyVo.setAreaAgentId(pidId.get(Constants.OrganizationType.AREAAGENT));
        }
        List<String> areaList = new ArrayList<>();
        areaList.add(organizationProxyVo.getAgentAreaProvince()); // 获取省
        areaList.add(organizationProxyVo.getAgentAreaCity()); // 获取市
        areaList.add(organizationProxyVo.getAgentAreaRegion()); //获取区
        organizationProxyVo.setAgentAreaList(areaList); //省市区凭借
    }

    @Override
    public OrganizationProxyVo getByProxyId(Long proxyId) {
        OrganizationProxyVo organizationProxyVo = this.tblAdminOrganizationMapper.findOneProxyById(proxyId);
        getProxyAllFather(organizationProxyVo, true);
        return organizationProxyVo;
    }

    @Override
    public List<Organization> findByOrgType(String orgType) {
        Condition condition = new Condition(Organization.class);
        buildValidCriteria(condition).andEqualTo("orgType", orgType);
        return this.findByCondition(condition);

    }

    @Override
    public ApiResult verification(OrganizationBusinessDto organizationBusinessDto) {
        if (Constants.BusinessType.PERSONAL.equals(organizationBusinessDto.getBusinessType())) {
            if (StringUtils.isEmpty(organizationBusinessDto.getName())) {
                return ApiResult.error("姓名不能为空");
            }
            if (StringUtils.isEmpty(organizationBusinessDto.getIdCard())) {
                return ApiResult.error("身份证不能为空");
            }
            if (organizationBusinessDto.getIdCard().length() != 18) {
                return ApiResult.error("身份证不能小于18位");
            }
        } else if (Constants.BusinessType.ENTERPRISE.equals(organizationBusinessDto.getBusinessType())) {
            if (StringUtils.isEmpty(organizationBusinessDto.getCompanyName())) {
                return ApiResult.error("公司名称不能为空");
            }
            if (StringUtils.isEmpty(organizationBusinessDto.getBusinessLicense())) {
                return ApiResult.error("工商营业执照不能为空");
            }
            if (StringUtils.isEmpty(organizationBusinessDto.getOrganizationCodeCertificate())) {
                return ApiResult.error("组织机构代码证不能为空");
            }
            if (StringUtils.isEmpty(organizationBusinessDto.getTaxRegistrationCertificate())) {
                return ApiResult.error("税务登记证不能为空");
            }
        }
        return ApiResult.success();
    }

    @Override
    public List<Map<String, Object>> buildAllOrganization(List<Organization> organizationList) {
        List<Map<String, Object>> list = new LinkedList<>();
        organizationList.stream().forEach(
                result -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", result.getId());
                    map.put("label", result.getOrgName());
                    map.put("orgType", result.getOrgType());
                    map.put("children", buildAllOrganization(findByPid(result.getId())));
                    list.add(map);
                }
        );
        return list;
    }

    @Override
    public Boolean repeatVerificationOrgCode(String orgCode, Long id) {
        return repeatVerification("orgCode", orgCode, id);
    }

    @Override
    public Boolean repeatVerificationOrgName(String orgName, Long id) {
        return repeatVerification("orgName", orgName, id);
    }

    @Override
    public Map<String, Object> saveProxy(OrganizationBusinessDto organizationBusinessDto) {
        setProxyPid(organizationBusinessDto);
        String cityCode;
        if (organizationBusinessDto.getCityAgentId() != null) {
            // stop  说明当前用户不是市代理
            Organization cityAgent = this.findById(organizationBusinessDto.getCityAgentId());
            if (cityAgent == null) {
                throw new ServiceException("市代理不存在");
            }
            cityCode = cityAgent.getOrgCode().substring(0, 5);
        } else {
            // stop 说明当前用户是市代理
            cityCode = organizationBusinessDto.getOrgCode().substring(0, 5);
        }

        Organization organization = this.findById(organizationBusinessDto.getPid());
        // stop
        if (organizationBusinessDto.getPid() == null) {
            log.info("当前代理的的父Id为{}", organizationBusinessDto.getPid());
            throw new ServiceException("上级机构部不存在");
        }
        if (StringUtils.isEmpty(organization.getCustomerNO())) { //没有上一级
            organization.setCustomerNO("0");
        }

        Long id = saveBusinessUnit(organizationBusinessDto);

        // stop 生成前端的系统账号和密码为 第三个参数传市代理的编码
        ApiResult<String> result = adminCustomerFeign.adminRegister(organizationBusinessDto.getRegisterMobile(), organization.getCustomerNO(),
                Long.valueOf(organization.getOrgCode()), Long.valueOf(organizationBusinessDto.getOrgCode()), Long.valueOf(cityCode));

        if (result.hasFail()) {
            log.info("生成前端代理账号失败");
            throw new ServiceException(result.getMsg());
        }

        log.info("生产手机端的用户为账号为{}", result.getData());
        Organization org = this.findById(id);
        OrganizationInfomation organizationInfomation = this.organizationInfomationService.findByOrganizationId(id);
        org.setCustomerNO(result.getData());
        if (StringUtils.isEmpty(organizationBusinessDto.getReturnAccount())) {
            organizationInfomation.setReturnAccount(result.getData());
        }
        this.update(org);
        organizationInfomationService.update(organizationInfomation);
        // stop 发送短信把用户账号和密码发给用户
//        OperatorSmsDto operatorSmsDto =new OperatorSmsDto();
//        operatorSmsDto.setSmsType("1");
//        operatorSmsDto.setPhone(organizationBusinessDto.getRegisterMobile());
//        operatorSmsDto.setContentNo("6");
//        smsFeign.operatorSms(operatorSmsDto);
        Map<String, Object> map = new HashMap<>();
        map.put("customerNo", result.getData());
        return map;
    }

    private void setProxyPid(OrganizationBusinessDto organizationBusinessDto) {
        // stop1 根据类型获取pid
        if (organizationBusinessDto.getOrganizationId() != null) {
            if (organizationBusinessDto.getOrgType().equals(Constants.OrganizationType.CITYAGENT)) {
                // stop2 如果为市代理则，设置机构为父Id
                organizationBusinessDto.setPid(organizationBusinessDto.getOrganizationId());
            } else if (organizationBusinessDto.getOrgType().equals(Constants.OrganizationType.AREAAGENT)) { // 为区代理,则选择市代理
                if (organizationBusinessDto.getOrganizationId() == null || organizationBusinessDto.getCityAgentId() == null) { // 市代理和机构代理不能为空
                    throw new ServiceException("机构或空市代理不能为空");
                }
                organizationBusinessDto.setPid(organizationBusinessDto.getCityAgentId());
            } else if (organizationBusinessDto.getOrgType().equals(Constants.OrganizationType.ORDINARYAGENT)) { // 为普通代理,则选择区代理Id
                if (organizationBusinessDto.getOrganizationId() == null || organizationBusinessDto.getCityAgentId() == null
                        || organizationBusinessDto.getAreaAgentId() == null) {
                    throw new ServiceException("机构,市代理或区代理不能为空");
                }
                organizationBusinessDto.setPid(organizationBusinessDto.getAreaAgentId());
            }
        } else { // stop 如果组织机构ID为空,说明代理只属于平台
            throw new ServiceException("请选择机构");
        }
    }

    @Override
    public void updateProxy(OrganizationBusinessDto organizationBusinessDto) {
        setProxyPid(organizationBusinessDto);
        updateBusinessUnit(organizationBusinessDto);
    }

    @Override
    public List<OrganizationBusinessVo> findAllFather(String orgId) {
        List<OrganizationBusinessVo> list = new ArrayList<>();
        findByAllFather(Long.valueOf(orgId), list);
        return list;
    }

    private void findByAllFather(Long id, List<OrganizationBusinessVo> organizationBusinessVos) {
        OrganizationBusinessVo organizationBusinessVo = this.tblAdminOrganizationMapper.findByBusinessId(id);
        if (organizationBusinessVo != null) {
            if (organizationBusinessVo.getPid() != null) {
                organizationBusinessVos.add(organizationBusinessVo);
                this.findByAllFather(organizationBusinessVo.getPid(), organizationBusinessVos);
            }
        }
    }

    @Override
    public Organization findByOrgCode(String orgCode) {
        Condition condition = new Condition(Organization.class);
        Example.Criteria criteria = buildValidCriteria(condition);
        criteria.andEqualTo("orgCode", orgCode);
        List<Organization> organizations = tblAdminOrganizationMapper.selectByCondition(condition);
        if (organizations.size() > 0) {
            return organizations.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<OrgNameVo> findByType(OrganizationTypeDto organizationTypeDto) {
        Condition condition = new Condition(Organization.class);
        Example.Criteria criteria = buildValidCriteria(condition);
        if (!StringUtils.isEmpty(organizationTypeDto.getOrgType())) {
            criteria.andEqualTo("orgType", organizationTypeDto.getOrgType());
        }
        List<Organization> organizations = tblAdminOrganizationMapper.selectByCondition(condition);
        List<OrgNameVo> orgNameVos = new ArrayList<>();
        for (Organization organization : organizations) {
            OrgNameVo orgNameVo = new OrgNameVo();
            orgNameVo.setOrganizationId(organization.getId());
            orgNameVo.setOrgName(organization.getOrgName());
            orgNameVos.add(orgNameVo);
        }
        return orgNameVos;
    }

    /**
     * 1、遍历对象,把值丢进数据库,如果传过来的值有,就替换原来的对象
     *
     * @param organizationBusinessDto
     */
    private void toOrgnizationEntity(Organization organization, OrganizationBusinessDto organizationBusinessDto) {
        organization.setOrgName(organizationBusinessDto.getOrgName());
        if (organizationBusinessDto.getPid() != null) {
            organization.setPid(organizationBusinessDto.getPid());
        }
        if (!StringUtils.isEmpty(organizationBusinessDto.getOrgType())) {
            organization.setOrgType(organizationBusinessDto.getOrgType());
        }
        //注册手机号
        organization.setRegisterMobile(organizationBusinessDto.getRegisterMobile());
        organization.setOrgCode(organizationBusinessDto.getOrgCode());
    }


    private void toEntity(OrganizationInfomation entity, OrganizationBusinessDto
            organizationBusinessDto) {

        entity.setBusinessType(organizationBusinessDto.getBusinessType()); // 商业类型

        if (organizationBusinessDto.getBusinessType().equals(Constants.BusinessType.ENTERPRISE)) {
            entity.setName(null); // 姓名
            entity.setIdCard(null); // 身份证
            entity.setBusinessLicense(organizationBusinessDto.getBusinessLicense()); // 工商营业证
            entity.setOrganizationCodeCertificate(organizationBusinessDto.getOrganizationCodeCertificate()); // 组织机构证
            entity.setTaxRegistrationCertificate(organizationBusinessDto.getTaxRegistrationCertificate());//税务登记证
            entity.setCompanyName(organizationBusinessDto.getCompanyName()); // 分公司名称
        } else if (organizationBusinessDto.getBusinessType().equals(Constants.BusinessType.PERSONAL)) {
            entity.setName(organizationBusinessDto.getName()); // 姓名
            entity.setIdCard(organizationBusinessDto.getIdCard()); // 身份证
            entity.setCompanyName(null);
            entity.setOrganizationCodeCertificate(null);
            entity.setTaxRegistrationCertificate(null);
            entity.setBusinessLicense(null);
        }
        entity.setReturnAccount(organizationBusinessDto.getReturnAccount()); // 返回账号
        entity.setOrganizationId(organizationBusinessDto.getId());// 组织机构
        entity.setRebateFreezeRatio(organizationBusinessDto.getRebateFreezeRatio()); //返回比例
        entity.setReferrer(organizationBusinessDto.getReferrer()); //平级推荐人
        if (!CollectionUtils.isEmpty(organizationBusinessDto.getAgentAreaList())) { // 区域list
            String area = getArea(organizationBusinessDto);
            entity.setAgentArea(area); // 代理区域
            List<String> agentArea = organizationBusinessDto.getAgentAreaList();
            if (agentArea.isEmpty()) {
                log.info("区域agentArea是{}", agentArea);
            }
            entity.setAgentAreaProvince(agentArea.get(0));//代理省
            entity.setAgentAreaCity(agentArea.get(1)); // 代理市
            entity.setAgentAreaRegion(agentArea.get(2)); //代理区域
        }
    }

    private String getArea(OrganizationBusinessDto organizationBusinessDto) {
        StringBuilder area = new StringBuilder();
        organizationBusinessDto.getAgentAreaList().stream().forEach(x -> {
            if (!StringUtils.isEmpty(x)) {
                area.append(x);
            }
        });
        area.append(organizationBusinessDto.getAgentArea());
        return area.toString();
    }

}


