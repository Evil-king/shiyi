package com.baibei.shiyi.admin.modules.content.controller;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.content.feign.bean.dto.*;
import com.baibei.shiyi.content.feign.bean.vo.VersionChannelVo;
import com.baibei.shiyi.content.feign.client.ContentFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/content/configuration")
public class ConfigurationController {

    @Autowired
    private ContentFeign contentFeign;

    @RequestMapping("/addLogistic")
    public ApiResult addLogistic(@RequestBody LogisticsDto logisticsDto){
        return contentFeign.addLogistics(logisticsDto);
    }


    @RequestMapping("/logisticsList")
    public ApiResult logisticsList(){
        return contentFeign.logisticsList();
    }


    @RequestMapping("/deleteLogistics")
    public ApiResult deleteLogistics(@RequestBody DeleteLogisticsDto deleteLogisticsDto){
        return contentFeign.deleteLogistics(deleteLogisticsDto);
    }

    @RequestMapping("/addConfiguration")
    public ApiResult configurationData(@RequestBody ConfigurationDto configurationDto){
        return contentFeign.configurationData(configurationDto);
    }

    @RequestMapping("/addConfigurationContent")
    public ApiResult addConfigurationContent(@RequestBody ConfigurationContentDto configurationDto){
        return contentFeign.addConfigurationContent(configurationDto);
    }

    @RequestMapping("/getConfiguration")
    public ApiResult getConfiguration(){
        return contentFeign.getConfiguration();
    }

    @RequestMapping("/getConfigurationContent")
    public ApiResult getConfigurationContent(){
        return contentFeign.getConfigurationContentAdmin();
    }


    @RequestMapping("/newPricePage")
    public ApiResult addNewPricePage(){
        return contentFeign.newPricePage();
    }


    @GetMapping("/versionChannel")
    public ApiResult<List<VersionChannelVo>> versionChannel(){
        return contentFeign.versionChannel();
    }



}
