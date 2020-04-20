package com.baibei.shiyi.admin.modules.account.web;

import com.baibei.shiyi.admin.modules.account.bean.dto.AccountBalanceRepeatDto;
import com.baibei.shiyi.admin.modules.account.bean.vo.AccountBalanceRepeatVo;
import com.baibei.shiyi.admin.modules.account.service.IAccountBalanceRepeatService;
import com.baibei.shiyi.admin.modules.utils.ExcelUtils;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/admin/accountBalanceRepeat")
public class AccountBalanceRepeatController {

    @Autowired
    private IAccountBalanceRepeatService accountBalanceRepeatService;

    @Autowired
    private ExcelUtils excelUtils;

    @PostMapping(path = "/pageList")
    public ApiResult<MyPageInfo<AccountBalanceRepeatVo>> pageList(@Validated @RequestBody AccountBalanceRepeatDto repeatDto) {
        MyPageInfo<AccountBalanceRepeatVo> accountBalanceRepeatVoMyPageInfo = accountBalanceRepeatService.findPageList(repeatDto);
        return ApiResult.success(accountBalanceRepeatVoMyPageInfo);
    }

    /**
     * 导出数据表格
     *
     * @param response
     * @param accountBalanceRepeatDto
     */
    @PostMapping(path = "/excelExport")
    public void excelExport(HttpServletResponse response, @RequestBody AccountBalanceRepeatDto accountBalanceRepeatDto) {
        MyPageInfo<AccountBalanceRepeatVo> pageList = accountBalanceRepeatService.findPageList(accountBalanceRepeatDto);
        excelUtils.defaultExcelExport(pageList.getList(), response, AccountBalanceRepeatVo.class, "账号余额重复数据表格");
    }
}
