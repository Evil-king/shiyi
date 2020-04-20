package com.baibei.shiyi.admin.modules.system.web;

import com.baibei.shiyi.admin.modules.system.bean.dto.OrganizationBusinessDto;
import com.baibei.shiyi.admin.modules.system.bean.dto.OrganizationBusinessPageDto;
import com.baibei.shiyi.admin.modules.system.bean.vo.OrganizationBranchVo;
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

@RequestMapping("/admin/organization/branch")
@RestController
public class AdminBranchOrganizationController {

    @Autowired
    private IOrganizationService organizationService;

    @Autowired
    private ExcelUtils excelUtils;

    @PostMapping(path = "/pageList")
    @PreAuthorize("hasAnyRole(@authorityExpression.find('ORGANIZATION_MANAGE_SUBCOMPANY'))")
    public ApiResult<MyPageInfo<OrganizationBranchVo>> pageList(@RequestBody @Validated OrganizationBusinessPageDto organizationBusinessPageDto) {
        organizationBusinessPageDto.setOrgType(Constants.OrganizationType.BRANCHOFFICE);
        return ApiResult.success(organizationService.pageListBranchCompany(organizationBusinessPageDto));
    }

    @PostMapping(path = "/save")
    @PreAuthorize("hasAnyRole(@authorityExpression.add('ORGANIZATION_MANAGE_SUBCOMPANY'))")
    public ApiResult save(@RequestBody @Validated OrganizationBusinessDto organizationBusinessDto) {
        ApiResult result = paramsValidation(organizationBusinessDto);
        if (!result.getCode().equals(ResultEnum.SUCCESS.getCode())) {
            return result;
        }
        organizationBusinessDto.setOrgType(Constants.OrganizationType.BRANCHOFFICE);
        this.organizationService.saveBranchCompany(organizationBusinessDto);
        return ApiResult.success();
    }

    /**
     * 获取分公司的信息
     *
     * @param organizationBusinessDto
     * @return
     */
    @PostMapping(path = "/getById")
    @PreAuthorize("hasAnyRole(@authorityExpression.find('ORGANIZATION_MANAGE_SUBCOMPANY'))")
    public ApiResult<OrganizationBranchVo> getById(@RequestBody OrganizationBusinessDto organizationBusinessDto) {
        if (organizationBusinessDto.getId() == null) {
            return ApiResult.error("分公司Id不能为空 ");
        }
        return ApiResult.success(this.organizationService.getBranchCompanyId(organizationBusinessDto.getId()));
    }

    /**
     * 修改分公司列表
     *
     * @param organizationBusinessDto
     * @return
     */
    @PostMapping(path = "/update")
    @PreAuthorize("hasAnyRole(@authorityExpression.edit('ORGANIZATION_MANAGE_SUBCOMPANY'))")
    public ApiResult updateBranchCompany(@Validated @RequestBody OrganizationBusinessDto organizationBusinessDto) {
        if (organizationBusinessDto.getId() == null) {
            return ApiResult.error("修改分公司的Id不能为空");
        }
        ApiResult result = paramsValidation(organizationBusinessDto);
        if (result.hasFail()) {
            return result;
        }
        this.organizationService.updateBranchCompany(organizationBusinessDto);
        return ApiResult.success();
    }

    private ApiResult paramsValidation(OrganizationBusinessDto organizationBusinessDto) {
        if (organizationBusinessDto.getPid() == null) {
            return ApiResult.error("所属机构不能为为空");
        }
        if (organizationBusinessDto.getOrgName() == null) {
            return ApiResult.error("分公司名称不能为空");
        }
        if (organizationBusinessDto.getOrgCode() == null) {
            return ApiResult.error("分公司编码不能为空");
        }
        if (!organizationBusinessDto.getOrgCode().matches("^[1-9]\\d{2}$")) {
            return ApiResult.error("分公司编码长度为三位数字,并且首位不为0");
        }
        return ApiResult.success();
    }


    @PostMapping(path = "/excelExport")
    @PreAuthorize("hasAnyRole(@authorityExpression.exportFile('ORGANIZATION_MANAGE_SUBCOMPANY'))")
    public void excelExport(@RequestBody OrganizationBusinessPageDto organizationBusinessPageDto, HttpServletResponse response) {
        organizationBusinessPageDto.setOrgType(Constants.OrganizationType.BRANCHOFFICE);
        MyPageInfo<OrganizationBranchVo> pageInfo = organizationService.pageListBranchCompany(organizationBusinessPageDto);
        excelUtils.defaultExcelExport(pageInfo.getList(), response, OrganizationBranchVo.class, "分公司管理");
    }

}
