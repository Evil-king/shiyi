package com.baibei.shiyi.admin.modules.content.controller;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.bean.CustomerBaseAndPageDto;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.content.feign.bean.dto.AdminPublicNoticeDto;
import com.baibei.shiyi.content.feign.bean.vo.PublicNoticeVo;
import com.baibei.shiyi.content.feign.client.IAdminPublicNoticeFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/content/public/notice")
public class PublicNoticeController {

    @Autowired
    private IAdminPublicNoticeFeign adminPublicNoticeFeign;

    @PostMapping(path = "/pageList")
    @PreAuthorize("hasAnyRole(@authorityExpression.find('NOTICES'))")
    public ApiResult<MyPageInfo<PublicNoticeVo>> pageList(@RequestBody CustomerBaseAndPageDto customerBaseAndPageDto) {
        return adminPublicNoticeFeign.pageList(customerBaseAndPageDto);
    }

    @PostMapping(path = "/getById")
    @PreAuthorize("hasAnyRole(@authorityExpression.find('NOTICES'))")
    public ApiResult<PublicNoticeVo> getById(@RequestBody AdminPublicNoticeDto publicNoticeDto) {
        return adminPublicNoticeFeign.getById(publicNoticeDto);
    }

    @PostMapping(path = "/deleteById")
    @PreAuthorize("hasAnyRole(@authorityExpression.delete('NOTICES'))")
    public ApiResult deleteById(@RequestBody AdminPublicNoticeDto helpCenterDto) {
        return adminPublicNoticeFeign.deleteById(helpCenterDto);
    }

    @PostMapping(path = "/save")
    @PreAuthorize("hasAnyRole(@authorityExpression.add('NOTICES'))")
    public ApiResult save(@Valid @RequestBody AdminPublicNoticeDto publicNoticeDto) {
        return adminPublicNoticeFeign.save(publicNoticeDto);
    }

    @PostMapping(path = "/update")
    @PreAuthorize("hasAnyRole(@authorityExpression.edit('NOTICES'))")
    public ApiResult update(@Valid @RequestBody AdminPublicNoticeDto publicNoticeDto) {
        return adminPublicNoticeFeign.update(publicNoticeDto);
    }

    @PostMapping(path = "/batchOperate")
    public ApiResult batchOperate(@RequestBody AdminPublicNoticeDto adminPublicNoticeDto) {
        if (StringUtils.isEmpty(adminPublicNoticeDto.getBatchType())) {
            return ApiResult.error("批量类型不能为空");
        }
        ApiResult apiResult;
        switch (adminPublicNoticeDto.getBatchType()) {
            case "delete":
                apiResult = adminPublicNoticeFeign.batchDelete(adminPublicNoticeDto);
                break;
            case "update":
                apiResult = adminPublicNoticeFeign.batchHidden(adminPublicNoticeDto);
                break;
            default:
                apiResult = ApiResult.error("操作类型不支持");
                break;
        }
        return apiResult;
    }

}
