package com.baibei.shiyi.admin.modules.content.controller;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.bean.CustomerBaseAndPageDto;
import com.baibei.shiyi.content.feign.bean.dto.UrlDto;
import com.baibei.shiyi.content.feign.client.ContentFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/urlManagement")
public class UrlManagementController {

    @Autowired
    private ContentFeign contentFeign;

    @PostMapping("/pageList")
    @PreAuthorize("hasAnyRole(@authorityExpression.find('LINKS'))")
    public ApiResult pageList(@RequestBody CustomerBaseAndPageDto pageParam){
        return contentFeign.pageList(pageParam);
    }

    @PostMapping("/insertUrl")
    @PreAuthorize("hasAnyRole(@authorityExpression.add('LINKS'))")
    public ApiResult insertUrl(@Validated @RequestBody UrlDto urlDto){
        return contentFeign.insertUrl(urlDto);
    }

    @PostMapping("/updateUrl")
    @PreAuthorize("hasAnyRole(@authorityExpression.edit('LINKS'))")
    public ApiResult updateUrl(@Validated @RequestBody UrlDto urlDto){
        return contentFeign.updateUrl(urlDto);
    }

    @PostMapping("/delete")
    @PreAuthorize("hasAnyRole(@authorityExpression.delete('LINKS'))")
    public ApiResult delete(@RequestBody List<String> id){
        return contentFeign.deleteUrl(id);
    }

    @PostMapping("/dataList")
    public ApiResult dataList(){
        return contentFeign.dataList();
    }
}
