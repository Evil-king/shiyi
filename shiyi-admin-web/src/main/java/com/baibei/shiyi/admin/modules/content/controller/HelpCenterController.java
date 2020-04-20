package com.baibei.shiyi.admin.modules.content.controller;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.bean.CustomerBaseAndPageDto;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.content.feign.bean.dto.AdminHelpCenterDto;
import com.baibei.shiyi.content.feign.bean.vo.HelpCenterVo;
import com.baibei.shiyi.content.feign.client.IAdminHelpCenterFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/content/help/center")
public class HelpCenterController {

    @Autowired
    private IAdminHelpCenterFeign adminHelpCenterFeign;


    @PostMapping(path = "/pageList")
    @PreAuthorize("hasAnyRole(@authorityExpression.find('HELPS'))")
    public ApiResult<MyPageInfo<HelpCenterVo>> pageList(@RequestBody CustomerBaseAndPageDto customerBaseAndPageDto) {
        return adminHelpCenterFeign.pageList(customerBaseAndPageDto);
    }

    @PostMapping(path = "/getById")
    @PreAuthorize("hasAnyRole(@authorityExpression.find('HELPS'))")
    public ApiResult<HelpCenterVo> getById(@RequestBody AdminHelpCenterDto helpCenterDto) {
        return adminHelpCenterFeign.getById(helpCenterDto);
    }

    @PostMapping(path = "/deleteById")
    @PreAuthorize("hasAnyRole(@authorityExpression.delete('HELPS'))")
    public ApiResult deleteById(@RequestBody AdminHelpCenterDto helpCenterDto) {
        return adminHelpCenterFeign.deleteById(helpCenterDto);
    }

    @PostMapping(path = "/save")
    @PreAuthorize("hasAnyRole(@authorityExpression.add('HELPS'))")
    public ApiResult save(@Valid @RequestBody AdminHelpCenterDto helpCenterDto) {
        return adminHelpCenterFeign.save(helpCenterDto);
    }

    @PostMapping(path = "/update")
    @PreAuthorize("hasAnyRole(@authorityExpression.edit('HELPS'))")
    public ApiResult update(@Valid @RequestBody AdminHelpCenterDto helpCenterDto) {
        return adminHelpCenterFeign.update(helpCenterDto);
    }

    @PostMapping(path = "/batchOperate")
    public ApiResult batchOperate(@RequestBody AdminHelpCenterDto adminPublicNoticeDto) {
        if (StringUtils.isEmpty(adminPublicNoticeDto.getBatchType())) {
            return ApiResult.error("批量类型不能为空");
        }
        ApiResult apiResult;
        switch (adminPublicNoticeDto.getBatchType()) {
            case "delete":
                apiResult = adminHelpCenterFeign.batchDelete(adminPublicNoticeDto);
                break;
            case "update":
                apiResult = adminHelpCenterFeign.batchHidden(adminPublicNoticeDto);
                break;
            default:
                apiResult = ApiResult.error("操作类型不支持");
                break;
        }
        return apiResult;
    }

}
