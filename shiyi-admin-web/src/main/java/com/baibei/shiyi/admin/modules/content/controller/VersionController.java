package com.baibei.shiyi.admin.modules.content.controller;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.content.feign.base.IContentBase;
import com.baibei.shiyi.content.feign.bean.dto.*;
import com.baibei.shiyi.content.feign.bean.vo.AndroidPageVo;
import com.baibei.shiyi.content.feign.bean.vo.IosPageListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/content/version")
public class VersionController {

    @Autowired
    private IContentBase contentBase;

    @RequestMapping("/iosCtroPage")
    @PreAuthorize("hasAnyRole(@authorityExpression.find('VERSION_CONTROL_IOS'))")
    public ApiResult<MyPageInfo<IosPageListVo>> iosCtroList(@RequestBody IOSCtroPageDto iosCtroPageDto){
        return contentBase.iosCtroList(iosCtroPageDto);
    }

    @RequestMapping("/addIosVersion")
    @PreAuthorize("hasAnyRole(@authorityExpression.add('VERSION_CONTROL_IOS'))")
    public ApiResult addIosVersion(@RequestBody IOSAddVersionDto iosAddVersionDto){
        return contentBase.addIosVersion(iosAddVersionDto);
    }

    @RequestMapping("/editIosVersion")
    @PreAuthorize("hasAnyRole(@authorityExpression.edit('VERSION_CONTROL_IOS'))")
    public ApiResult editIosVersion(@RequestBody IOSAddVersionDto iosAddVersionDto){
        return contentBase.editIosVersion(iosAddVersionDto);
    }

    @RequestMapping("/deleIosVersion")
    @PreAuthorize("hasAnyRole(@authorityExpression.delete('VERSION_CONTROL_IOS'))")
    public ApiResult deleIosVersion(@RequestBody IOSAddVersionDto iosAddVersionDto){
        return contentBase.deleIosVersion(iosAddVersionDto);
    }

    @RequestMapping("/androidCtroPage")
    @PreAuthorize("hasAnyRole(@authorityExpression.find('VERSION_CONTROL_ANDROID'))")
    public ApiResult<MyPageInfo<AndroidPageVo>> androidCtroPage(@RequestBody AndroidPageDto androidPageDto){
        return contentBase.androidCtroPage(androidPageDto);
    }

    @RequestMapping("/androidEdit")
    @PreAuthorize("hasAnyRole(@authorityExpression.edit('VERSION_CONTROL_ANDROID'))")
    public ApiResult androidEdit(@RequestBody AndroidEditUpdateDto androidEditUpdateDto){
        return contentBase.androidEdit(androidEditUpdateDto);
    }

    @RequestMapping("/androidAdd")
    @PreAuthorize("hasAnyRole(@authorityExpression.add('VERSION_CONTROL_ANDROID'))")
    public ApiResult androidAdd(@RequestBody AndroidEditUpdateDto androidEditUpdateDto){
        return contentBase.androidAdd(androidEditUpdateDto);
    }

    @RequestMapping("/deleAndroidVersion")
    @PreAuthorize("hasAnyRole(@authorityExpression.delete('VERSION_CONTROL_ANDROID'))")
    public ApiResult deleAndroidVersion(@RequestBody AndroidEditLookDto androidEditLookDto){
        return contentBase.deleAndroidVersion(androidEditLookDto);
    }

}
