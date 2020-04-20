package com.baibei.shiyi.admin.modules.account.web;

import com.baibei.shiyi.admin.modules.account.bean.dto.AccountBalanceErrorDto;
import com.baibei.shiyi.admin.modules.account.bean.vo.AccountBalanceErrorVo;
import com.baibei.shiyi.admin.modules.account.service.IAccountBalanceErrorService;
import com.baibei.shiyi.admin.modules.utils.ExcelUtils;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/admin/accountBalanceError")
public class AccountBalanceErrorController {

    @Autowired
    private IAccountBalanceErrorService accountBalanceErrorService;

    @Autowired
    private ExcelUtils excelUtils;

    @PostMapping(path = "/pageList")
    public ApiResult<MyPageInfo<AccountBalanceErrorVo>> pageList(@Validated @RequestBody AccountBalanceErrorDto accountBalanceError) {
        MyPageInfo<AccountBalanceErrorVo> pageList = accountBalanceErrorService.findPageList(accountBalanceError);
        return ApiResult.success(pageList);
    }

    @PostMapping(path = "/excelExport")
    public void excelExport(HttpServletResponse response, @RequestBody AccountBalanceErrorDto accountBalanceErrorDto) {
        MyPageInfo<AccountBalanceErrorVo> pageList = accountBalanceErrorService.findPageList(accountBalanceErrorDto);
        excelUtils.defaultExcelExport(pageList.getList(), response, AccountBalanceErrorVo.class, "账号余额错误的数据");
    }

}
