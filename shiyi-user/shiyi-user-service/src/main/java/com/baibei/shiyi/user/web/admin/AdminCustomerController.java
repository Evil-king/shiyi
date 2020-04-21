package com.baibei.shiyi.user.web.admin;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.common.tool.utils.BeanUtil;
import com.baibei.shiyi.user.common.dto.CustomerPageListDto;
import com.baibei.shiyi.user.common.vo.CustomerPageListVo;
import com.baibei.shiyi.user.feign.base.admin.IAdminCustomerBase;
import com.baibei.shiyi.user.feign.bean.dto.*;
import com.baibei.shiyi.user.feign.bean.vo.AdminCustomerBalanceVo;
import com.baibei.shiyi.user.feign.bean.vo.CustomerListVo;
import com.baibei.shiyi.user.feign.bean.vo.CustomerVo;
import com.baibei.shiyi.user.model.Customer;
import com.baibei.shiyi.user.service.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: hyc
 * @date: 2019/7/31 15:10
 * @description:
 */
@RestController
public class AdminCustomerController implements IAdminCustomerBase {
    @Autowired
    private ICustomerService customerService;
    public ApiResult<MyPageInfo<CustomerPageListVo>> pageList(@RequestBody CustomerPageListDto customerPageListDto){

        return ApiResult.success(customerService.adminPageList(customerPageListDto));
    }

    @Override
    public ApiResult<MyPageInfo<AdminCustomerBalanceVo>> getCustomerPageList(@RequestBody  AdminCustomerAccountDto adminCustomerAccountDto) {
        return ApiResult.success(customerService.getCustomerBalanceList(adminCustomerAccountDto));
    }

    @Override
    public ApiResult<List<AdminCustomerBalanceVo>> getCustomerList(@RequestBody AdminCustomerAccountDto adminCustomerAccountDto) {
        return ApiResult.success(customerService.getCustomerList(adminCustomerAccountDto));
    }

    @Override
    public ApiResult<MyPageInfo<CustomerListVo>> getAllCustomerList(@RequestBody @Validated CustomerListDto customerListDto) {
        return ApiResult.success(customerService.getAllCustomerList(customerListDto));
    }

    @Override
    public ApiResult<List<CustomerListVo>> export(@RequestBody  CustomerListDto customerListDto) {
        return ApiResult.success(customerService.export(customerListDto));
    }

    @Override
    public ApiResult changeStatus(@RequestBody @Validated ChangeStatusDto changeStatusDto) {
        return customerService.changeStatus(changeStatusDto);
    }

    @Override
    public ApiResult changeRecommend(@RequestBody @Validated ChangeRecommendDto changeRecommendDto) {
        return customerService.changeRecommend(changeRecommendDto);
    }

    @Override
    public ApiResult<List<CustomerVo>> findByCustomerNoList(@RequestBody List<String> customerNos) {
        List<CustomerVo> customerVos=new ArrayList<>();
        for (int i = 0; i <customerNos.size() ; i++) {
            Customer customer = customerService.findByCustomerNo(customerNos.get(i));
            customerVos.add(BeanUtil.copyProperties(customer,CustomerVo.class));
        }
        return ApiResult.success(customerVos);
    }
    @Override
    public ApiResult<String> adminRegister(@RequestParam("mobile") String mobile, @RequestParam("recommenderNo") String recommenderNo, @RequestParam("superOrgCode") Long superOrgCode,@RequestParam("myOrgCode") Long myOrgCode,@RequestParam("cityAgentCode")Long cityAgentCode) {
        return customerService.adminRegister(mobile,recommenderNo,superOrgCode,myOrgCode,cityAgentCode);
    }

    @Override
    public ApiResult changeUser(@RequestBody List<ChangeUserDto> changeUserDto) {
        return customerService.changeUser(changeUserDto);
    }
}
