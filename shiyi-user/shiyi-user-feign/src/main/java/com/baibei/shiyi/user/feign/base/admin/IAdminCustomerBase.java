package com.baibei.shiyi.user.feign.base.admin;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.user.feign.bean.dto.*;
import com.baibei.shiyi.user.feign.bean.vo.AdminCustomerBalanceVo;
import com.baibei.shiyi.user.feign.bean.vo.CustomerListVo;
import com.baibei.shiyi.user.feign.bean.vo.CustomerVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: hyc
 * @date: 2019/11/1 14:04
 * @description:
 */
public interface IAdminCustomerBase {
    /**
     * 后台列表，根据用户编号、手机号、直推获取用户信息
     */
    @PostMapping("/shiyi/admin/customer/getCustomer")
    @ResponseBody
    ApiResult<MyPageInfo<AdminCustomerBalanceVo>> getCustomerPageList(@RequestBody AdminCustomerAccountDto adminCustomerAccountDto);

    /**
     * 后台列表，根据用户编号、手机号、直推获取用户信息(导出专用)
     */
    @PostMapping("/shiyi/admin/customer/getCustomerList")
    @ResponseBody
    ApiResult<List<AdminCustomerBalanceVo>> getCustomerList(@RequestBody AdminCustomerAccountDto adminCustomerAccountDto);

    @PostMapping("/shiyi/admin/customer/getAllCustomerList")
    @ResponseBody
    ApiResult<MyPageInfo<CustomerListVo>> getAllCustomerList(@RequestBody @Validated CustomerListDto customerListDto);

    @PostMapping("/shiyi/admin/customer/export")
    @ResponseBody
    ApiResult<List<CustomerListVo>> export(@RequestBody CustomerListDto customerListDto);

    @PostMapping("/shiyi/admin/customer/changeStatus")
    @ResponseBody
    ApiResult changeStatus(@RequestBody @Validated ChangeStatusDto changeStatusDto);

    @PostMapping("/shiyi/admin/customer/changeRecommend")
    @ResponseBody
    ApiResult changeRecommend(@RequestBody @Validated ChangeRecommendDto changeRecommendDto);

    @RequestMapping(value = "/shiyi/admin/customer/findByCustomerNos")
    @ResponseBody
    ApiResult<List<CustomerVo>> findByCustomerNoList(@RequestBody List<String> customerNos);

    /**
     * 后台注册商城用户
     * @param mobile 手机号
     * @param recommenderNo 直推用户编号（不存在时填0）
     * @param superOrgCode 直属归属（直属机构编码）
     * @return
     */
    @RequestMapping(value="/shiyi/admin/customer/register")
    @ResponseBody
    ApiResult<String> adminRegister(@RequestParam("mobile") String mobile, @RequestParam("recommenderNo")String recommenderNo,@RequestParam("superOrgCode") Long superOrgCode,@RequestParam("myOrgCode") Long myOrgCode,@RequestParam("cityAgentCode")Long cityAgentCode);

    @RequestMapping(value="/shiyi/admin/customer/changeUser")
    @ResponseBody
    ApiResult changeUser(@RequestBody List<ChangeUserDto> changeUserDto);
}
