package com.baibei.shiyi.cash.feign.client.admin;

import com.baibei.shiyi.cash.feign.base.dto.DepositWithDrawDto;
import com.baibei.shiyi.cash.feign.base.vo.DepositWithdrawVo;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "${shiyi-cash:shiyi-cash}", path = "/admin/cash/deposit/withdraw")
public interface IAdminDepositWithdrawFeign {

    @PostMapping(path = "/pageList")
    ApiResult<MyPageInfo<DepositWithdrawVo>> pageList(@Validated @RequestBody DepositWithDrawDto depositWithDrawDto);
}
