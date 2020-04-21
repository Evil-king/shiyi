package com.baibei.shiyi.user.feign.client.hystrix;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.user.feign.bean.dto.*;
import com.baibei.shiyi.user.feign.bean.vo.AdminCustomerBalanceVo;
import com.baibei.shiyi.user.feign.bean.vo.CustomerListVo;
import com.baibei.shiyi.user.feign.bean.vo.CustomerVo;
import com.baibei.shiyi.user.feign.client.CustomerFeign;
import com.baibei.shiyi.user.feign.client.admin.AdminCustomerFeign;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author: hyc
 * @date: 2019/11/1 14:57
 * @description:
 */
@Component
@Slf4j
public class AdminCustomerHystrix implements FallbackFactory<AdminCustomerFeign> {
    @Override
    public AdminCustomerFeign create(Throwable cause) {
        return new AdminCustomerFeign() {
            @Override
            public ApiResult<MyPageInfo<AdminCustomerBalanceVo>> getCustomerPageList(AdminCustomerAccountDto adminCustomerAccountDto) {
                log.info("getCustomerPageList fallback,reason was : {}",cause);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<List<AdminCustomerBalanceVo>> getCustomerList(AdminCustomerAccountDto adminCustomerAccountDto) {
                log.info("getCustomerList fallback,reason was : {}",cause);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<MyPageInfo<CustomerListVo>> getAllCustomerList(CustomerListDto customerListDto) {
                log.info("getAllCustomerList fallback,reason was : {}",cause);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<List<CustomerListVo>> export(CustomerListDto customerListDto) {
                log.info("export fallback,reason was : {}",cause);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult changeStatus(ChangeStatusDto changeStatusDto) {
                log.info("changeStatus fallback,reason was : {}",cause);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult changeRecommend(ChangeRecommendDto changeRecommendDto) {
                log.info("changeRecommend fallback,reason was : {}",cause);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<List<CustomerVo>> findByCustomerNoList(List<String> customerNos) {
                log.info("findByCustomerNoList fallback,reason was : {}",cause);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<String> adminRegister(String mobile, String recommenderNo,Long orgCode,Long myOrgCode,Long superCode) {
                log.info("adminRegister fallback,reason was : {}",cause);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult changeUser(List<ChangeUserDto> changeUserDto) {
                log.info("changeUser fallback,reason was : {}",cause);
                return ApiResult.serviceFail();
            }
        };
    }
}
