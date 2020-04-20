package com.baibei.shiyi.cash.web.api;

import com.baibei.shiyi.cash.feign.base.dto.DepositWithDrawDto;
import com.baibei.shiyi.cash.feign.base.vo.DepositWithdrawVo;
import com.baibei.shiyi.cash.service.IDepositWithdrawService;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/api/cash/depositWithdraw")
public class ApiDepositAndWithdrawController {

    @Autowired
    private IDepositWithdrawService depositWithdrawService;

    @PostMapping("/pageList")
    public ApiResult<MyPageInfo<DepositWithdrawVo>> pageList(@RequestBody DepositWithDrawDto depositWithDrawDto) {
        return ApiResult.success(depositWithdrawService.pageList(depositWithDrawDto));
    }


}
