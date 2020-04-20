package com.baibei.shiyi.admin.modules.system.service;

import com.baibei.shiyi.admin.modules.system.bean.dto.OrganizationBusinessDto;
import com.baibei.shiyi.admin.modules.system.bean.dto.OrganizationBusinessPageDto;
import com.baibei.shiyi.admin.modules.system.bean.dto.OrganizationTypeDto;
import com.baibei.shiyi.admin.modules.system.bean.vo.*;
import com.baibei.shiyi.admin.modules.system.model.Organization;
import com.baibei.shiyi.common.core.mybatis.Service;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;

import java.util.List;
import java.util.Map;


/**
 * @author: hyc
 * @date: 2019/10/10 09:51:43
 * @description: Organization服务接口
 */
public interface IOrganizationService extends Service<Organization> {

    /**
     * 事业部列表
     *
     * @param organizationBusinessDto
     * @return
     */
    MyPageInfo<OrganizationBusinessVo> businessPageList(OrganizationBusinessPageDto organizationBusinessDto);

    Long saveBusinessUnit(OrganizationBusinessDto organizationBusinessDto);

    void updateBusinessUnit(OrganizationBusinessDto organizationBusinessDto);

    List<OrganizationBusinessVo> businessList(OrganizationBusinessPageDto organizationBusinessPageDto);

    /**
     * 机构列表
     *
     * @param organizationBusinessPageDto
     * @return
     */
    MyPageInfo<OrganizationAgencyVo> agencyPageList(OrganizationBusinessPageDto organizationBusinessPageDto);

    List<OrganizationAgencyVo> agencyList(OrganizationBusinessPageDto organizationBusinessPageDto);

    /**
     * 获取组织机构信息Id
     *
     * @param orgId 组织机构Id
     * @return
     */
    OrganizationAgencyVo getByAgencyId(Long orgId);

    /**
     * 根据父id获取详细信息
     *
     * @param pid
     * @return
     */
    List<Organization> findByPid(Long pid);


    /**
     * 查询某种类型和pid为当前
     *
     * @param pid
     * @param orgType
     * @return
     */
    List<Organization> findByOrgTypeAndPid(Long pid, String orgType);

    /**
     * 分公司列表
     *
     * @param organizationBusinessPageDto
     * @return
     */
    MyPageInfo<OrganizationBranchVo> pageListBranchCompany(OrganizationBusinessPageDto organizationBusinessPageDto);

    /**
     * 是否有子菜单
     *
     * @return
     */
    Boolean isHaveChildren(Long orgId);

    /**
     * 保存分公司
     *
     * @param organizationBusinessDto
     */
    void saveBranchCompany(OrganizationBusinessDto organizationBusinessDto);

    /**
     * 修改分公司信息
     */
    void updateBranchCompany(OrganizationBusinessDto organizationBusinessDto);


    /**
     * 获取分公司信息
     *
     * @param branchCompanyId
     * @return
     */
    OrganizationBranchVo getBranchCompanyId(Long branchCompanyId);


    /**
     * 分公司代理
     *
     * @return
     */
    MyPageInfo<OrganizationProxyVo> pageListProxy(OrganizationBusinessPageDto organizationBusinessPageDto);


    /**
     * 分公司代理
     *
     * @param organizationBusinessPageDto
     * @return
     */
    MyPageInfo<OrganizationProxyVo> pageListExportProxy(OrganizationBusinessPageDto organizationBusinessPageDto);

    /**
     * 获取分公司的详细信息
     *
     * @param proxyId
     * @return
     */
    OrganizationProxyVo getByProxyId(Long proxyId);

    /**
     * 根据机构类型获取
     *
     * @param orgType
     * @return
     */
    List<Organization> findByOrgType(String orgType);

    List<OrgNameVo> findByType(OrganizationTypeDto organizationTypeDto);

    /**
     * 参数验证
     *
     * @param organizationBusinessDto
     * @return
     */
    ApiResult verification(OrganizationBusinessDto organizationBusinessDto);


    /**
     * 构建所有的机构
     *
     * @param organizationList
     * @return
     */
    List<Map<String, Object>> buildAllOrganization(List<Organization> organizationList);


    /**
     * 验证机构编码是否重复
     *
     * @param orgCode
     * @return
     */
    Boolean repeatVerificationOrgCode(String orgCode, Long id);

    /**
     * 验证机构名称是否重复
     *
     * @param orgName
     * @return
     */
    Boolean repeatVerificationOrgName(String orgName, Long id);

    /**
     * 保存代理
     */
    Map<String, Object> saveProxy(OrganizationBusinessDto organizationBusinessDto);

    /**
     * 修改代理
     *
     * @param organizationBusinessDto
     */
    void updateProxy(OrganizationBusinessDto organizationBusinessDto);

    /**
     * 通过机构id获取他的所有上级机构信息
     *
     * @param orgId
     * @return
     */
    List<OrganizationBusinessVo> findAllFather(String orgId);


    /**
     * 根据机构代码获取信息
     *
     * @param orgCode
     * @return
     */
    Organization findByOrgCode(String orgCode);
}

