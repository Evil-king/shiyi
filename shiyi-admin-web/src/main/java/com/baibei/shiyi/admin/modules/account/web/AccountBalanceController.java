package com.baibei.shiyi.admin.modules.account.web;


import com.baibei.component.redis.util.RedisUtil;
import com.baibei.shiyi.admin.modules.account.bean.dto.AccountBalanceDto;
import com.baibei.shiyi.admin.modules.account.bean.vo.AccountBalanceExportVo;
import com.baibei.shiyi.admin.modules.account.bean.vo.AccountBalanceVo;
import com.baibei.shiyi.admin.modules.account.model.AccountBalance;
import com.baibei.shiyi.admin.modules.account.model.AccountBalanceRepeat;
import com.baibei.shiyi.admin.modules.account.service.IAccountBalanceRepeatService;
import com.baibei.shiyi.admin.modules.account.service.IAccountBalanceService;
import com.baibei.shiyi.admin.modules.security.utils.SecurityUtils;
import com.baibei.shiyi.admin.modules.utils.ExcelUtils;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.api.ResultEnum;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.common.tool.utils.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.baibei.shiyi.common.tool.api.ResultEnum.ADMIN_SC_ACCOUNT_BALANCE_ERROR;

@RestController
@RequestMapping("/admin/accountBalance")
@Slf4j
public class AccountBalanceController {

    @Autowired
    private IAccountBalanceService accountBalanceService;

    @Autowired
    private ExcelUtils excelUtils;

    @Autowired
    private IAccountBalanceRepeatService accountBalanceRepeatService;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 分页列表
     *
     * @return
     */
    @PostMapping(path = "/pageList")
    @PreAuthorize("hasAnyRole(@authorityExpression.find('ASSET_MANAGE_USER_BALANCE_MANAGE'))")
    public ApiResult<MyPageInfo<AccountBalanceVo>> pageList(@Validated @RequestBody AccountBalanceDto accountBalanceDto) {
        return ApiResult.success(accountBalanceService.pageList(accountBalanceDto));
    }


    /**
     * 保存
     *
     * @param accountBalanceDto
     * @return
     */
    @PostMapping(path = "/save")
    public ApiResult save(@Validated @RequestBody AccountBalanceDto accountBalanceDto) {
        accountBalanceService.saveAccountBalance(accountBalanceDto);
        return ApiResult.success();
    }

    /**
     * 修改
     *
     * @param accountBalanceDto
     * @return
     */
    @PostMapping(path = "/update")
    public ApiResult update(@RequestBody @Validated AccountBalanceDto accountBalanceDto) {
        if (accountBalanceDto.getId() == null) {
            return ApiResult.error("id不能为空");
        }
        accountBalanceService.updateAccountBalance(accountBalanceDto);
        return ApiResult.success();
    }

    /**
     * 导入文件
     *
     * @return
     */
    @PostMapping(path = "/importFile")
    @PreAuthorize("hasAnyRole(@authorityExpression.exportFile('ASSET_MANAGE_USER_BALANCE_MANAGE'))")
    public ApiResult importFile(@RequestParam(value = "file") MultipartFile multipartFile) {
        String batchNo = generateBatchNo("M");
        if(log.isDebugEnabled()){
            log.debug("当前的批次号为{}",batchNo);
        }
        List<AccountBalanceExportVo> accountBalanceVos = excelUtils.defaultReadExcelFile(multipartFile, AccountBalanceExportVo.class);
        Boolean result = accountBalanceService.batchSave(accountBalanceVos, batchNo);
        if (!result) {
            return ApiResult.build(ADMIN_SC_ACCOUNT_BALANCE_ERROR.getCode(), ADMIN_SC_ACCOUNT_BALANCE_ERROR.getMsg(), null);
        }
        return ApiResult.success();
    }

    /**
     * 余额执行
     *
     * @return
     */
    @PostMapping(path = "/execution")
    @PreAuthorize("hasAnyRole(@authorityExpression.execute('ASSET_MANAGE_USER_BALANCE_MANAGE'))")
    public ApiResult execution(@RequestBody AccountBalanceDto accountBalanceDto) {
        if (accountBalanceDto.getId() == null) {
            return ApiResult.error("Id不能为空");
        }
        return accountBalanceService.execution(accountBalanceDto.getId());
    }

    /**
     * 校验重复的数据
     *
     * @param accountBalanceDto
     * @return
     */
    @PostMapping(path = "/checkDuplicate")
    public ApiResult checkDuplicate(@RequestBody AccountBalanceDto accountBalanceDto) {
        if (accountBalanceDto.getIds() == null || CollectionUtils.isEmpty(accountBalanceDto.getIds())) {
            return ApiResult.error("id不能为空");
        }
        List<AccountBalance> accountBalances = this.accountBalanceService.findByIds(accountBalanceDto.getIds());

        if (!this.accountBalanceService.verifyStatus(accountBalances)) {
            return ApiResult.error("当前的数据有成功或者超时的数据,请重新选择");
        }

        List<AccountBalance> duplicateList = accountBalances.stream().filter(
                x -> this.accountBalanceService.isDuplicate(x)).collect(Collectors.toList());

        // 说明当前有重复或者已经执行的数据
        if (!CollectionUtils.isEmpty(duplicateList)) {
            List<AccountBalanceRepeat> accountBalanceRepeats = duplicateList.stream().map(x -> toEntity(x)).collect(Collectors.toList());
            accountBalanceRepeatService.save(accountBalanceRepeats);
            return ApiResult.build(ResultEnum.ADMIN_SC_DUPLICATE, null);
        }
        // 返回成功
        return ApiResult.success();
    }


    /**
     * 删除
     *
     * @return
     */
    @PostMapping(path = "/delete")
    @PreAuthorize("hasAnyRole(@authorityExpression.delete('ASSET_MANAGE_USER_BALANCE_MANAGE'))")
    public ApiResult delete(@RequestBody AccountBalanceDto accountBalanceDto) {
        if (accountBalanceDto.getId() == null) {
            return ApiResult.error("删除id不能为空");
        }
        if (Constants.SuccessOrFail.SUCCESS.equals(accountBalanceDto.getStatus())) {
            return ApiResult.error("已经执行成功,不能删除");
        }
        this.accountBalanceService.softDeleteById(accountBalanceDto.getId());
        log.info("执行删除的当前用户为{},时间为{}", SecurityUtils.getUsername(), LocalDateTime.now().toString());
        return ApiResult.success();
    }

    /**
     * 批量删除
     *
     * @return
     */
    @PostMapping(path = "/batchDelete")
    @PreAuthorize("hasAnyRole(@authorityExpression.delete('ASSET_MANAGE_USER_BALANCE_MANAGE'))")
    public ApiResult batchDelete(@RequestBody AccountBalanceDto accountBalanceDto) {
        if (accountBalanceDto.getIds() == null || accountBalanceDto.getIds().isEmpty()) {
            return ApiResult.error("批量删除id不能为空");
        }
        this.accountBalanceService.batchDelete(accountBalanceDto.getIds());
        log.info("执行批量删除的当前用户为{},时间为{}", SecurityUtils.getUsername(), LocalDateTime.now().toString());
        return ApiResult.success();
    }

    /**
     * 批量执行
     *
     * @return
     */
    @PostMapping(path = "/batchExecute")
    @PreAuthorize("hasAnyRole(@authorityExpression.execute('ASSET_MANAGE_USER_BALANCE_MANAGE'))")
    public ApiResult batchExecute(@RequestBody AccountBalanceDto accountBalanceDto) {
        if (CollectionUtils.isEmpty(accountBalanceDto.getIds())) {
            return ApiResult.error("批量执行Id不能为空");
        }
        return accountBalanceService.batchExecution(accountBalanceDto);
    }

    /**
     * 导出文件
     *
     * @return
     */
    @PostMapping(path = "/exportFile")
    @PreAuthorize("hasAnyRole(@authorityExpression.importFile('ASSET_MANAGE_USER_BALANCE_MANAGE'))")
    public void exportFile(HttpServletResponse response, @RequestBody AccountBalanceDto accountBalanceDto) {
        MyPageInfo<AccountBalanceVo> accountBalanceVoMyPageInfo = this.accountBalanceService.pageList(accountBalanceDto);
        excelUtils.defaultExcelExport(accountBalanceVoMyPageInfo.getList(), response, AccountBalanceVo.class, "商城账户管理");
    }

    /**
     * 模板下载
     *
     * @return
     */
    @PostMapping(path = "/templateDownload")
    @PreAuthorize("hasAnyRole(@authorityExpression.importFile('ASSET_MANAGE_USER_BALANCE_MANAGE'))")
    public void templateDownload(HttpServletResponse response) {
        List<AccountBalanceExportVo> accountBalanceList = new ArrayList<>();
        excelUtils.defaultExcelExport(accountBalanceList, response, AccountBalanceExportVo.class, "商城账户管理");
    }

    private AccountBalanceRepeat toEntity(AccountBalance accountBalance) {
        AccountBalanceRepeat accountBalanceRepeat = new AccountBalanceRepeat();
        accountBalanceRepeat.setId(IdWorker.getId()); // 自增长主键
        accountBalanceRepeat.setBalance(accountBalance.getBalance()); // 余额
        accountBalanceRepeat.setBalanceType(accountBalance.getBalanceType()); //类型
        accountBalanceRepeat.setCreateBy(accountBalance.getCreateBy());  //创建时间
        accountBalanceRepeat.setCustomerNo(accountBalance.getCustomerNo()); // 客户编号
        accountBalanceRepeat.setExecutionBy(accountBalance.getExecutionBy()); // 执行者
        accountBalanceRepeat.setExecutionTime(accountBalance.getExecutionTime()); // 执行时间
        accountBalanceRepeat.setPhone(accountBalance.getPhone()); // 手机号
        accountBalanceRepeat.setStatus(accountBalance.getStatus());  // 状态
        accountBalanceRepeat.setBatchNo(accountBalance.getBatchNo()); // 批次号
        return accountBalanceRepeat;
    }


    /**
     * 生成批次号
     *
     * @return
     */
    private String generateBatchNo(String prefix) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String key = prefix + formatter.format(LocalDate.now());
        StringBuilder batchNoStr = new StringBuilder(key);
        Long No;
        if (!redisUtil.existKey(key)) {
            No = redisUtil.increment(key, 1);
            redisUtil.expireDate(key, 1L);
        } else {
            No = redisUtil.increment(key, 1);
        }
        batchNoStr.append(String.format("%03d", No));
        String batchNo = batchNoStr.toString();
        Boolean repeatVerification = this.accountBalanceService.repeatVerification("batchNo", batchNo, null);
        if (repeatVerification) {
            return generateBatchNo(prefix);
        }
        return batchNo;
    }
}
