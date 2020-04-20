package com.baibei.shiyi.admin.modules.system.web;

import com.baibei.shiyi.admin.modules.system.bean.dto.OrganizationBusinessDto;
import com.baibei.shiyi.admin.modules.system.bean.dto.OrganizationBusinessPageDto;
import com.baibei.shiyi.admin.modules.system.bean.vo.OrganizationBusinessVo;
import com.baibei.shiyi.admin.modules.system.service.IOrganizationService;
import com.baibei.shiyi.admin.modules.utils.ExcelUtils;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.api.ResultEnum;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * @author: hyc
 * @date: 2019/10/10 10:09
 * @description:
 */
@RestController
@RequestMapping("/admin/organization/business")
public class AdminBusinessOrganizationController {

    @Autowired
    private IOrganizationService organizationService;

    @Autowired
    private ExcelUtils excelUtils;

//    @RequestMapping("/pageList")
//    public ApiResult<MyPageInfo<OrganizationBusinessVo>> pageList(@Validated @RequestBody OrganizationBusinessPageDto organizationBusinessDto) {
//        return ApiResult.success(organizationService.businessPageList(organizationBusinessDto));
//    }


    /**
     * 增加事业部
     *
     * @return
     */
    @PostMapping("/save")
    @PreAuthorize("hasAnyRole(@authorityExpression.add('ORGANIZATION_MANAGE_BUSINESS_UNIT'))")
    public ApiResult save(@Validated @RequestBody OrganizationBusinessDto organizationBusinessDto) {
        if (organizationBusinessDto.getOrgName() == null) {
            return ApiResult.error("事业部名称不能为空");
        }
        ApiResult result = paramsValidation(organizationBusinessDto);
        if (!result.getCode().equals(ResultEnum.SUCCESS.getCode())) {
            return result;
        }
        organizationBusinessDto.setOrgType(Constants.OrganizationType.BUSINESS);
        organizationBusinessDto.setPid(0L);
        organizationService.saveBusinessUnit(organizationBusinessDto);
        return ApiResult.success();
    }

    /**
     * 修改事业部
     *
     * @param organizationBusinessDto
     * @return
     */
    @PostMapping("/update")
    @PreAuthorize("hasAnyRole(@authorityExpression.edit('ORGANIZATION_MANAGE_BUSINESS_UNIT'))")
    public ApiResult update(@Validated @RequestBody OrganizationBusinessDto organizationBusinessDto) {
        if (organizationBusinessDto.getId() == null) {
            return ApiResult.error("事业部Id不能为空");
        }
        organizationService.updateBusinessUnit(organizationBusinessDto);
        return ApiResult.success();
    }

    private ApiResult paramsValidation(OrganizationBusinessDto organizationBusinessDto) {
        if (organizationBusinessDto.getOrgName() == null) {
            return ApiResult.error("事业部名称不能为空");
        }
        if (organizationBusinessDto.getOrgCode() == null) {
            return ApiResult.error("事业部名称编码不能为空");
        }
        if (!organizationBusinessDto.getOrgCode().matches("^\\d{2,9}$")) {
            return ApiResult.error("事业部编号最大9位,最少2位");
        }
        return ApiResult.success();
    }

    /**
     * 事业部列表
     *
     * @return
     */
    @PostMapping("/pageList")
    @PreAuthorize("hasAnyRole(@authorityExpression.find('ORGANIZATION_MANAGE_BUSINESS_UNIT'))")
    public ApiResult<MyPageInfo<OrganizationBusinessVo>> pageList(@Validated @RequestBody OrganizationBusinessPageDto organizationBusinessDto) {
        organizationBusinessDto.setOrgType(Constants.OrganizationType.BUSINESS);
        return ApiResult.success(organizationService.businessPageList(organizationBusinessDto));
    }


    /**
     * 事业部导出
     *
     * @param organizationBusinessDto
     * @return
     */
    @PostMapping(path = "/excelExport", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @PreAuthorize("hasAnyRole(@authorityExpression.exportFile('ORGANIZATION_MANAGE_BUSINESS_UNIT'))")
    public void excelExport(@RequestBody OrganizationBusinessPageDto organizationBusinessDto, HttpServletResponse response) {
        organizationBusinessDto.setOrgType(Constants.OrganizationType.BUSINESS);
        List<OrganizationBusinessVo> organizationBusinessVoList = organizationService.businessList(organizationBusinessDto);
        excelUtils.defaultExcelExport(organizationBusinessVoList, response, OrganizationBusinessVo.class, "事业部管理");
    }
}
