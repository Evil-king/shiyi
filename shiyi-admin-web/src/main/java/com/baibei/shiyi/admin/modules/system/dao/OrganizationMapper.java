package com.baibei.shiyi.admin.modules.system.dao;

import com.baibei.shiyi.admin.modules.system.bean.dto.OrganizationBusinessPageDto;
import com.baibei.shiyi.admin.modules.system.bean.vo.OrganizationAgencyVo;
import com.baibei.shiyi.admin.modules.system.bean.vo.OrganizationBranchVo;
import com.baibei.shiyi.admin.modules.system.bean.vo.OrganizationBusinessVo;
import com.baibei.shiyi.admin.modules.system.bean.vo.OrganizationProxyVo;
import com.baibei.shiyi.admin.modules.system.model.Organization;
import com.baibei.shiyi.common.core.mybatis.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface OrganizationMapper extends MyMapper<Organization> {

    /**
     * 事业部列表
     *
     * @param organizationBusinessDto
     * @return
     */
    List<OrganizationBusinessVo> businessPageList(OrganizationBusinessPageDto organizationBusinessDto);


    /**
     * 机构列表
     */
    List<OrganizationAgencyVo> agencyPageList(OrganizationBusinessPageDto organizationBusinessPageDto);


    /**
     * 分公司列表
     *
     * @param organizationBusinessPageDto
     * @return
     */
    List<OrganizationBranchVo> findByBranchCompanyPageList(OrganizationBusinessPageDto organizationBusinessPageDto);

    /**
     * 获取所有的父类
     *
     * @param childrenId
     * @return
     */
    List<OrganizationBusinessVo> findByAllFather(@Param("childrenId") Long childrenId);


    /**
     * 根据id获取
     *
     * @return
     */
    OrganizationBusinessVo findByBusinessId(@RequestParam("orgId") Long orgId);

    /**
     * 根据组织机构获取信息
     *
     * @param orgId
     * @return
     */
    OrganizationAgencyVo findByAgencyId(@Param("orgId") Long orgId);


    /**
     * 查询分公司信息
     *
     * @param branchCompanyId
     * @return
     */
    OrganizationBranchVo findByOneBranchCompanyId(Long branchCompanyId);


    /**
     * 查询分公司关联的代理
     *
     * @param branchCompanyId
     * @return
     */
    List<Organization> findByBranchCompanyId(@Param("branchCompanyId") Long branchCompanyId, @Param("orgType") String orgType);

    /**
     * 查询分公司和父id代表的机构
     */
    List<Organization> findByBranchCompanyIdAndPid(@Param("branchCompanyId") Long branchCompanyId, @Param("pid") Long pid);

    /**
     * 查询代理列表
     *
     * @return
     */
    List<OrganizationProxyVo> findByProxyList(OrganizationBusinessPageDto organizationBusinessPageDto);

    /**
     * 根据代理Id获取详细信息
     *
     * @param proxyId
     * @return
     */
    OrganizationProxyVo findOneProxyById(@Param("proxyId") Long proxyId);
}