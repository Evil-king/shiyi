package com.baibei.shiyi.admin.modules.system.web;

import com.baibei.shiyi.admin.modules.system.bean.dto.OrganizationBusinessDto;
import com.baibei.shiyi.admin.modules.system.bean.dto.OrganizationTypeDto;
import com.baibei.shiyi.admin.modules.system.bean.vo.OrgNameVo;
import com.baibei.shiyi.admin.modules.system.model.Organization;
import com.baibei.shiyi.admin.modules.system.service.IOrganizationService;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 组织机构共有的一些api
 */
@RestController
@RequestMapping("/admin/organization")
public class AdminOrganizationController {

    @Autowired
    private IOrganizationService organizationService;


    @PostMapping(path = "/deleteById")
    public ApiResult deleteById(@RequestBody OrganizationBusinessDto organizationBusinessDto) {
        if (organizationBusinessDto.getId() == null) {
            return ApiResult.error("机构Id不能为空");
        }
        if (this.organizationService.isHaveChildren(organizationBusinessDto.getId())) {
            return ApiResult.error("组织机构下级还存在,无法删除");
        }
        this.organizationService.softDeleteById(organizationBusinessDto.getId());
        return ApiResult.success();
    }

    /**
     * 根据机构类型获取信息
     */
    @PostMapping(path = "/findByOrgType")
    public ApiResult<List<Map<String, Object>>> findByOrgType(@RequestBody OrganizationBusinessDto organizationBusinessDto) {
        if (StringUtils.isEmpty(organizationBusinessDto.getOrgType())) {
            return ApiResult.error("机构类型不能为空");
        }
        List<Map<String, Object>> result = organizationService.findByOrgType(organizationBusinessDto.getOrgType()).stream().map(organization -> organizationConversion(organization)
        ).collect(Collectors.toList());
        return ApiResult.success(result);
    }

    /**
     * 根据父id查询子类
     *
     * @return
     */
    @PostMapping(path = "/findByPid")
    public ApiResult<List<Map<String, Object>>> findByPid(@RequestBody OrganizationBusinessDto organizationBusinessDto) {
        if (organizationBusinessDto.getPid() == null) {
            return ApiResult.error("父类不能为空");
        }
        if (organizationBusinessDto.getOrgType() == null) {
            return ApiResult.error("机构类型不能为空");
        }
        List<Map<String, Object>> result = organizationService.findByOrgTypeAndPid(organizationBusinessDto.getPid(), organizationBusinessDto.getOrgType()).stream().map(
                organization -> organizationConversion(organization)).collect(Collectors.toList());
        return ApiResult.success(result);
    }

    /**
     * 根据组织机构转换Map 对象
     *
     * @param organization
     * @return
     */
    private Map<String, Object> organizationConversion(Organization organization) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("id", organization.getId());
        resultMap.put("orgName", "[" + organization.getOrgCode() + "]" + organization.getOrgName());
        resultMap.put("orgType", organization.getOrgType());
        return resultMap;
    }

    /**
     * 通过机构类型获取机构id 名称等信息
     */
    @PostMapping("/findByType")
    public ApiResult<List<OrgNameVo>> findByType(@RequestBody @Validated OrganizationTypeDto organizationTypeDto) {
        return ApiResult.success(organizationService.findByType(organizationTypeDto));
    }

    /**
     * 构建所有的市代理形成层级结构,也可以获取其他组织机构类型下面的所有的子集
     *
     * @return
     */
    @PostMapping(path = "/buildAllCityOrganization")
    public ApiResult<List<Map<String, Object>>> buildAllCityOrganization() {
        List<Map<String, Object>> result = this.organizationService.buildAllOrganization(organizationService.findByOrgType(Constants.OrganizationType.CITYAGENT));
        return ApiResult.success(result);
    }

}
