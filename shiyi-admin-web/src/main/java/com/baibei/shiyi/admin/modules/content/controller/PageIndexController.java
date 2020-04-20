package com.baibei.shiyi.admin.modules.content.controller;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.content.feign.bean.dto.*;
import com.baibei.shiyi.content.feign.client.ContentFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/home")
public class PageIndexController {

    @Autowired
    private ContentFeign contentFeign;

    @RequestMapping(value = "/operatorIndex")
    public ApiResult operatorIndex(@RequestBody List<String> message){
        return contentFeign.operatorIndex(message);
    }

    @GetMapping("/index")
    public ApiResult operatorIndex(){
        return contentFeign.index();
    }

    @PostMapping("/logo")
    public ApiResult operatorLogo(@RequestBody HomeLogoDto homeLogoDto){

        return contentFeign.operatorLogo(homeLogoDto);
    }

    @PostMapping("/banner")
    public ApiResult operatorBanner(@RequestBody HomeBannerDto homeBannerDto){

        return contentFeign.operatorBanner(homeBannerDto);
    }

    @PostMapping("/navigation")
    public ApiResult operatorNavigation(@RequestBody HomeNavigationDto homeNavigationDto){

        return contentFeign.operatorNavigation(homeNavigationDto);
    }

    @PostMapping("/notice")
    public ApiResult operatorNavigation(@RequestBody HomeNoticeDto homeNoticeDto){

        return contentFeign.operatorNavigation(homeNoticeDto);
    }

    @PostMapping("/activity")
    public ApiResult operatorActivity(@RequestBody HomeActivityDto homeActivityDto){

        return contentFeign.operatorActivity(homeActivityDto);
    }

    @PostMapping("/product")
    public ApiResult operatorProduct(@RequestBody HomeProductDto homeProductDto){

        return contentFeign.operatorProduct(homeProductDto);
    }
}
