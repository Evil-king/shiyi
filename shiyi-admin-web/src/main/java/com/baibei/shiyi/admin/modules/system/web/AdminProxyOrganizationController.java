package com.baibei.shiyi.admin.modules.system.web;

import com.baibei.shiyi.account.feign.bean.dto.CustomerNoDto;
import com.baibei.shiyi.admin.modules.system.bean.dto.OrganizationBusinessDto;
import com.baibei.shiyi.admin.modules.system.bean.dto.OrganizationBusinessPageDto;
import com.baibei.shiyi.admin.modules.system.bean.vo.OrganizationProxyVo;
import com.baibei.shiyi.admin.modules.system.service.IOrganizationService;
import com.baibei.shiyi.admin.modules.utils.ExcelUtils;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.api.ResultEnum;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.user.feign.client.shiyi.ICustomerFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 机构代理
 */
@RestController
@RequestMapping("/admin/organization/proxy")
public class AdminProxyOrganizationController {

    @Autowired
    private IOrganizationService organizationService;

    @Autowired
    private ExcelUtils excelUtils;

    @Autowired
    private ICustomerFeign customerFeign;


    @PostMapping(path = "/pageList")
    @PreAuthorize("hasAnyRole(@authorityExpression.find('ORGANIZATION_MANAGE_AGENT'))")
    public ApiResult<MyPageInfo<OrganizationProxyVo>> pageList(@RequestBody @Validated OrganizationBusinessPageDto organizationBusinessPageDto) {
        return ApiResult.success(organizationService.pageListExportProxy(organizationBusinessPageDto));
    }

    //保存代理
    @PostMapping(path = "/save")
    @PreAuthorize("hasAnyRole(@authorityExpression.add('ORGANIZATION_MANAGE_AGENT'))")
    public ApiResult<Map<String, Object>> save(@RequestBody @Validated OrganizationBusinessDto organizationBusinessDto) {
        ApiResult checkResult = checkParameter(organizationBusinessDto);
        if (!checkResult.getCode().equals(ResultEnum.SUCCESS.getCode())) {
            return checkResult;
        }
        Map<String, Object> saveResult = organizationService.saveProxy(organizationBusinessDto);
        return ApiResult.success(saveResult);
    }

    // 获取代理的信息
    @PostMapping(path = "/getById")
    @PreAuthorize("hasAnyRole(@authorityExpression.find('ORGANIZATION_MANAGE_AGENT'))")
    public ApiResult<OrganizationProxyVo> getById(@RequestBody @Validated OrganizationBusinessDto organizationBusinessDto) {
        if (organizationBusinessDto.getId() == null) {
            return ApiResult.error("代理Id不能为空");
        }
        return ApiResult.success(organizationService.getByProxyId(organizationBusinessDto.getId()));
    }

    // 修改代理
    @PostMapping(path = "/update")
    @PreAuthorize("hasAnyRole(@authorityExpression.edit('ORGANIZATION_MANAGE_AGENT'))")
    public ApiResult update(@RequestBody @Validated OrganizationBusinessDto organizationBusinessDto) {
        if (organizationBusinessDto.getId() == null) {
            return ApiResult.error("代理Id不能为空");
        }
        ApiResult checkResult = checkParameter(organizationBusinessDto);
        if (!checkResult.getCode().equals(ResultEnum.SUCCESS.getCode())) {
            return checkResult;
        }
        organizationService.updateProxy(organizationBusinessDto);
        return ApiResult.success();
    }

    @PostMapping(path = "/excelExport")
    @PreAuthorize("hasAnyRole(@authorityExpression.exportFile('ORGANIZATION_MANAGE_AGENT'))")
    public void excelExport(@RequestBody OrganizationBusinessPageDto organizationBusinessPageDto, HttpServletResponse response) {
        List<OrganizationProxyVo> organizationProxyVoList = organizationService.pageListExportProxy(organizationBusinessPageDto).getList();
        excelUtils.defaultExcelExport(organizationProxyVoList, response, OrganizationProxyVo.class, "代理管理");
    }

    private ApiResult checkParameter(OrganizationBusinessDto organizationBusinessDto) {
        if (organizationBusinessDto.getOrgName() == null) {
            return ApiResult.error("代理名称不能为空");
        }
        if (organizationBusinessDto.getOrgType() == null) {
            return ApiResult.error("代理类型不能为空");
        }

        if (organizationBusinessDto.getOrgCode() == null) {
            return ApiResult.error("代理编号不能为空");
        }
        if (!organizationBusinessDto.getOrgCode().matches("^[1-9]\\d{4,9}$")) {
            return ApiResult.error("代理编码不为字母,最大10位，最低5位，且首位不为0");
        }

        if (CollectionUtils.isEmpty(organizationBusinessDto.getAgentAreaList())) {
            return ApiResult.error("代理区域不能为空");
        }
        if (StringUtils.isEmpty(organizationBusinessDto.getRegisterMobile())) {
            return ApiResult.error("新注册手机号不能为空");
        }

        if (!StringUtils.isEmpty(organizationBusinessDto.getReferrer())) {
            CustomerNoDto customerNoDto = new CustomerNoDto();
            customerNoDto.setCustomerNo(organizationBusinessDto.getReferrer().toString());
            ApiResult apiResult = customerFeign.findCustomerNo(customerNoDto);
            if (apiResult.hasFail()) {
                return ApiResult.error("平级推荐人不存在");
            }
        }

        if (!StringUtils.isEmpty(organizationBusinessDto.getReturnAccount())) {
            CustomerNoDto customerNoDto = new CustomerNoDto();
            customerNoDto.setCustomerNo(organizationBusinessDto.getReturnAccount());
            ApiResult apiResult = customerFeign.findCustomerNo(customerNoDto);
            if (apiResult.hasFail()) {
                return ApiResult.error("返还账号不存在");
            }
        }
        return ApiResult.success();
    }


}
