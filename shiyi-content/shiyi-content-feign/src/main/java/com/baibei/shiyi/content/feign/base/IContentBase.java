package com.baibei.shiyi.content.feign.base;


import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.bean.CustomerBaseAndPageDto;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.content.feign.bean.dto.*;
import com.baibei.shiyi.content.feign.bean.vo.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

public interface IContentBase {

    /**
     * 首页装修logo区域
     * @param homeLogoDto
     * @return
     */
    @RequestMapping(value = "/admin/content/home/logo", method = RequestMethod.POST)
    @ResponseBody
    ApiResult operatorLogo(@RequestBody HomeLogoDto homeLogoDto);

    /**
     *  首页装修banner区域
     * @param homeBannerDto
     * @return
     */
    @RequestMapping(value = "/admin/content/home/banner", method = RequestMethod.POST)
    @ResponseBody
    ApiResult operatorBanner(@RequestBody HomeBannerDto homeBannerDto);

    /**
     * 首页装修navigation区域
     * @param homeNavigationDto
     * @return
     */
    @RequestMapping(value = "/admin/content/home/navigation", method = RequestMethod.POST)
    @ResponseBody
    ApiResult operatorNavigation(@RequestBody HomeNavigationDto homeNavigationDto);

    /**
     * 首页装修notice区域
     * @param homeNoticeDto
     * @return
     */
    @RequestMapping(value = "/admin/content/home/notice", method = RequestMethod.POST)
    @ResponseBody
    ApiResult operatorNavigation(@RequestBody HomeNoticeDto homeNoticeDto);

    /**
     * 首页装修activity区域
     * @param homeActivityDto
     * @return
     */
    @RequestMapping(value = "/admin/content/home/activity", method = RequestMethod.POST)
    @ResponseBody
    ApiResult operatorActivity(@RequestBody HomeActivityDto homeActivityDto);

    /**
     * 首页装修product区域
     * @param homeProductDto
     * @return
     */
    @RequestMapping(value = "/admin/content/home/product", method = RequestMethod.POST)
    @ResponseBody
    ApiResult operatorProduct(@RequestBody HomeProductDto homeProductDto);

    /**
     * 获取装修product详情信息
     * @return
     */
    @RequestMapping(value = "/admin/content/home/getHomeProductIndex", method = RequestMethod.GET)
    @ResponseBody
    ApiResult<List<HomeProductDetailsIndexVo>> getHomeProductIndex();


    /**
     * 首页装修operatorIndex
     * @param message
     * @return
     */
    @RequestMapping(value = "/admin/content/home/operatorIndex", method = RequestMethod.POST)
    @ResponseBody
    ApiResult operatorIndex(@RequestBody List<String> message);

    /**
     * 首页装修index
     * @return
     */
    @RequestMapping(value = "/admin/content/home/index", method = RequestMethod.GET)
    @ResponseBody
    ApiResult index();

    /**
     * url查询
     * @param pageParam
     * @return
     */
    @RequestMapping(value = "/admin/content/urlManagement/pageList", method = RequestMethod.POST)
    @ResponseBody
    ApiResult pageList(@RequestBody CustomerBaseAndPageDto pageParam);

    /**
     * url管理列表
     * @return
     */
    @RequestMapping(value = "/admin/content/urlManagement/dataList", method = RequestMethod.POST)
    @ResponseBody
    ApiResult dataList();

    /**
     * 新增url
     * @param urlDto
     * @return
     */
    @RequestMapping(value = "/admin/content/urlManagement/insertUrl", method = RequestMethod.POST)
    @ResponseBody
    ApiResult insertUrl(@RequestBody UrlDto urlDto);


    /**
     * 更新url
     * @param urlDto
     * @return
     */
    @RequestMapping(value = "/admin/content/urlManagement/updateUrl", method = RequestMethod.POST)
    @ResponseBody
    ApiResult updateUrl(@RequestBody UrlDto urlDto);

    /**
     * 删除url
     * @param id
     * @return
     */
    @RequestMapping(value = "/admin/content/urlManagement/delete", method = RequestMethod.POST)
    @ResponseBody
    ApiResult deleteUrl(@RequestBody List<String> id);

    /**
     * 意见反馈列表
     * @param feedBackDto
     * @return
     */
    @RequestMapping(value = "/admin/content/feedback/pageList", method = RequestMethod.POST)
    @ResponseBody
    ApiResult feedBackPageList(@RequestBody FeedBackDto feedBackDto);

    /**
     * 意见反馈导出
     * @param feedBackDto
     * @return
     */
    @RequestMapping(value = "/admin/content/feedback/exportData", method = RequestMethod.POST)
    @ResponseBody
    ApiResult<List<FeedBackExportVo>> exportData(@RequestBody FeedBackDto feedBackDto);

    /**
     * 删除意见反馈
     * @param deleteFeedbackDto
     * @return
     */
    @RequestMapping(value = "/admin/content/feedback/deleteFeedback", method = RequestMethod.POST)
    @ResponseBody
    ApiResult deleteFeedback(@RequestBody DeleteFeedbackDto deleteFeedbackDto);

    /**
     * 参数配置-新增物流
     * @param logisticsDto
     * @return
     */
    @RequestMapping(value = "/admin/content/configuration/addLogistics", method = RequestMethod.POST)
    @ResponseBody
    ApiResult addLogistics(@RequestBody LogisticsDto logisticsDto);

    /**
     * 参数配置-物流列表
     * @return
     */
    @RequestMapping(value = "/admin/content/configuration/logisticsList", method = RequestMethod.GET)
    @ResponseBody
    ApiResult logisticsList();

    /**
     * 参数配置-删除物流
     * @param deleteLogisticsDto
     * @return
     */
    @RequestMapping(value = "/admin/content/configuration/deleteLogistics", method = RequestMethod.POST)
    @ResponseBody
    ApiResult deleteLogistics(@RequestBody DeleteLogisticsDto deleteLogisticsDto);

    /**
     * 参数配置-获取配置列表
     * @return
     */
    @RequestMapping(value = "/admin/content/configuration/getConfiguration",method = RequestMethod.GET)
    ApiResult getConfiguration();

    /**
     * 参数配置-获取内容列表
     * @return
     */
    @RequestMapping(value = "/admin/content/configuration/getConfigurationContent",method = RequestMethod.GET)
    ApiResult<ConfigurationContentVo> getConfigurationContentAdmin();

    /**
     * 参数配置-新增参数
     * @param configurationDto
     * @return
     */
    @RequestMapping(value = "/admin/content/configuration/addConfiguration",method = RequestMethod.POST)
    ApiResult configurationData(@RequestBody ConfigurationDto configurationDto);


    /**
     * 参数配置-新增内容参数
     * @param configurationDto
     * @return
     */
    @RequestMapping(value = "/admin/content/configuration/addConfigurationContent",method = RequestMethod.POST)
    ApiResult addConfigurationContent(@RequestBody ConfigurationContentDto configurationDto);



    /**
     * 获取商品最新价列表
     * @return
     */
    @RequestMapping(value = "/admin/content/configuration/newPricePage",method = RequestMethod.GET)
    ApiResult newPricePage();

    /**
     * IOS版本列表数据
     * @return
     */
    @RequestMapping(value = "/admin/content/version/iosCtroPage",method = RequestMethod.POST)
    ApiResult<MyPageInfo<IosPageListVo>> iosCtroList(@RequestBody IOSCtroPageDto iosCtroPageDto);

    /**
     * 新增IOS渠道版本
     * @param iosAddVersionDto
     * @return
     */
    @RequestMapping(value = "/admin/content/version/addIosVersion",method = RequestMethod.POST)
    ApiResult addIosVersion(@RequestBody IOSAddVersionDto iosAddVersionDto);


    /**
     * 编辑IOS渠道版本
     * @param iosAddVersionDto
     * @return
     */
    @RequestMapping(value = "/admin/content/version/editIosVersion",method = RequestMethod.POST)
    ApiResult editIosVersion(@RequestBody IOSAddVersionDto iosAddVersionDto);

    /**
     * 删除IOS版本
     * @param iosAddVersionDto
     * @return
     */
    @RequestMapping(value = "/admin/content/version/deleIosVersion",method = RequestMethod.POST)
    ApiResult deleIosVersion(@RequestBody IOSAddVersionDto iosAddVersionDto);

    /**
     * Android列表
     * @param androidPageDto
     * @return
     */
    @RequestMapping(value = "/admin/content/version/androidCtroPage",method = RequestMethod.POST)
    ApiResult<MyPageInfo<AndroidPageVo>> androidCtroPage(@RequestBody AndroidPageDto androidPageDto);

    /**
     * Android版本编辑
     * @param androidEditUpdateDto
     * @return
     */
    @RequestMapping(value = "/admin/content/version/androidEdit",method = RequestMethod.POST)
    ApiResult androidEdit(@RequestBody AndroidEditUpdateDto androidEditUpdateDto);


    /**
     * Android版本新增
     * @param androidEditUpdateDto
     * @return
     */
    @RequestMapping(value = "/admin/content/version/androidAdd",method = RequestMethod.POST)
    ApiResult androidAdd(@RequestBody AndroidEditUpdateDto androidEditUpdateDto);


    /**
     * Android版本删除
     * @param androidEditLookDto
     * @return
     */
    @RequestMapping(value = "/admin/content/version/deleAndroidVersion",method = RequestMethod.POST)
    ApiResult deleAndroidVersion(@RequestBody AndroidEditLookDto androidEditLookDto);

    /**
     * Android渠道
     * @return
     */
    @RequestMapping(value = "/admin/content/configuration/versionChannel",method = RequestMethod.GET)
    ApiResult<List<VersionChannelVo>> versionChannel();

    /**
     * 提取仓单记录获取最小审核数量
     * @return
     */
    @RequestMapping(value = "/admin/content/configuration/extractProduct/auditQuantity",method = RequestMethod.GET)
    ApiResult<Integer> getAuditQuantity();

    /**
     * 修改提取仓单最小审核数量
     * @param updateAuditQuantityDto
     * @return
     */
    @RequestMapping(value = "/admin/content/configuration/extractProduct/updateAuditQuantity",method = RequestMethod.POST)
    ApiResult updateAuditQuantity(@RequestBody @Validated UpdateAuditQuantityDto updateAuditQuantityDto);


    /***********************************************内部接口*****************************************************/

    @RequestMapping(value = "/shiyi/content/configuration/getConfigurationContent",method = RequestMethod.GET)
    ApiResult<ConfigurationContentVo> getConfigurationContent();

    @RequestMapping(value = "/shiyi/content/configuration/consignmentConfiguration",method = RequestMethod.GET)
    ApiResult<ConsignmentVo> consignmentConfiguration();

    @RequestMapping(value = "/shiyi/content/configuration/consignmentList",method = RequestMethod.POST)
    ApiResult<List<ConsignmentGoodsVo>> consignmentList(@RequestBody ConsignmentListDto consignmentListDto);

    @RequestMapping(value = "/shiyi/content/configuration/cashConfiguration",method = RequestMethod.GET)
    ApiResult<WithdrawVo> cashConfiguration();

    @RequestMapping(value = "/shiyi/content/configuration/passCardExchangeRate",method = RequestMethod.GET)
    ApiResult<PassCardExchangeRateVo> passCardExchangeRate();

    @RequestMapping(value = "/shiyi/content/configuration/extractProduct",method = RequestMethod.GET)
    ApiResult<ExtractProductVo> extractProduct();


}
