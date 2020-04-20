package com.baibei.shiyi.admin.modules.system.web;

import com.baibei.shiyi.admin.modules.system.bean.dto.OrganizationBusinessDto;
import com.baibei.shiyi.admin.modules.system.bean.dto.OrganizationBusinessPageDto;
import com.baibei.shiyi.admin.modules.system.bean.vo.OrganizationAgencyVo;
import com.baibei.shiyi.admin.modules.system.service.IOrganizationService;
import com.baibei.shiyi.admin.modules.utils.ExcelUtils;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.api.ResultEnum;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 机构构造器
 */
@RestController
@RequestMapping("/admin/organization/agency")
public class AdminAgencyOrganizationController {

    @Autowired
    private IOrganizationService organizationService;

    @Autowired
    private ExcelUtils excelUtils;

    /**
     * 组织机构列表
     *
     * @param organizationBusinessPageDto
     * @return
     */
    @PostMapping(path = "/pageList")
    @PreAuthorize("hasAnyRole(@authorityExpression.find('ORGANIZATION_MANAGE_INSTITUTIONAL_QUERY'))")
    public ApiResult<MyPageInfo<OrganizationAgencyVo>> pageList(@RequestBody @Validated OrganizationBusinessPageDto organizationBusinessPageDto) {
        organizationBusinessPageDto.setOrgType(Constants.OrganizationType.ORGANIZATION);
        return ApiResult.success(organizationService.agencyPageList(organizationBusinessPageDto));
    }


    @PostMapping(path = "/save")
    @PreAuthorize("hasAnyRole(@authorityExpression.add('ORGANIZATION_MANAGE_INSTITUTIONAL_QUERY'))")
    public ApiResult save(@RequestBody @Validated OrganizationBusinessDto organizationBusinessDto) {
        ApiResult apiResult = paramsValidation(organizationBusinessDto);
        if (!apiResult.getCode().equals(ResultEnum.SUCCESS.getCode())) {
            return apiResult;
        }
        organizationBusinessDto.setOrgType(Constants.OrganizationType.ORGANIZATION);
        organizationService.saveBusinessUnit(organizationBusinessDto);
        return ApiResult.success();
    }

    /**
     * 修改组织机构
     *
     * @return
     */
    @PostMapping(path = "/update")
    @PreAuthorize("hasAnyRole(@authorityExpression.edit('ORGANIZATION_MANAGE_INSTITUTIONAL_QUERY'))")
    public ApiResult updateOrganization(@Validated @RequestBody OrganizationBusinessDto organizationBusinessDto) {
        if (organizationBusinessDto.getId() == null) {
            return ApiResult.error("组织机构Id不能为空");
        }
        ApiResult apiResult = paramsValidation(organizationBusinessDto);
        if (!apiResult.getCode().equals(ResultEnum.SUCCESS.getCode())) {
            return apiResult;
        }
        organizationBusinessDto.setOrgType(Constants.OrganizationType.ORGANIZATION);
        organizationService.updateBusinessUnit(organizationBusinessDto);
        return ApiResult.success();
    }

    private ApiResult paramsValidation(OrganizationBusinessDto organizationBusinessDto) {
        if (organizationBusinessDto.getPid() == null) {
            return ApiResult.error("机构所属事业部不能为空");
        }
        if (organizationBusinessDto.getOrgName() == null) {
            return ApiResult.error("机构名称不能为空");
        }
        if (organizationBusinessDto.getOrgCode() == null) {
            return ApiResult.error("机构编号不能为空");
        }
        if (!organizationBusinessDto.getOrgCode().matches("^\\d{2,9}$")) {
            return ApiResult.error("机构编号最大9位,最少2位");
        }
        return ApiResult.success();
    }

    /**
     * 根据Id查看组织机构信息
     *
     * @param organizationBusinessDto
     * @return
     */
    @PostMapping(path = "/getById")
    public ApiResult<OrganizationAgencyVo> getByOrganizationId(@RequestBody OrganizationBusinessDto organizationBusinessDto) {
        if (organizationBusinessDto.getId() == null) {
            return ApiResult.error("组织机构Id不能为空");
        }
        return ApiResult.success(this.organizationService.getByAgencyId(organizationBusinessDto.getId()));
    }

    /**
     * 机构管理导出excel
     *
     * @param organizationBusinessPageDto
     * @param response
     */
    @PostMapping(path = "/excelExport")
    @PreAuthorize("hasAnyRole(@authorityExpression.exportFile('ORGANIZATION_MANAGE_INSTITUTIONAL_QUERY'))")
    public void excelExport(@RequestBody OrganizationBusinessPageDto organizationBusinessPageDto, HttpServletResponse response) {
        organizationBusinessPageDto.setOrgType(Constants.OrganizationType.ORGANIZATION);
        List<OrganizationAgencyVo> organizationAgencyVoMyPageInfo = organizationService.agencyList(organizationBusinessPageDto);
        excelUtils.defaultExcelExport(organizationAgencyVoMyPageInfo, response, OrganizationAgencyVo.class, "组织机构管理");
    }


}
