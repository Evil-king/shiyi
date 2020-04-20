package com.baibei.shiyi.admin.modules.account.service;

import com.baibei.shiyi.admin.modules.account.bean.dto.AccountBalanceDto;
import com.baibei.shiyi.admin.modules.account.bean.vo.AccountBalanceExportVo;
import com.baibei.shiyi.admin.modules.account.bean.vo.AccountBalanceVo;
import com.baibei.shiyi.admin.modules.account.model.AccountBalance;
import com.baibei.shiyi.common.core.mybatis.Service;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;

import java.util.List;


/**
 * @author: uqing
 * @date: 2019/11/26 15:41:32
 * @description: AccountBalance服务接口
 */
public interface IAccountBalanceService extends Service<AccountBalance> {

    /**
     * 分页
     *
     * @param accountBalanceDto
     * @return
     */
    MyPageInfo<AccountBalanceVo> pageList(AccountBalanceDto accountBalanceDto);

    /**
     * 批量保存数据
     *
     * @param accountBalanceList
     */
    Boolean batchSave(List<AccountBalanceExportVo> accountBalanceList, String batchNo);

    /**
     * 校验重复
     *
     * @return
     */
    Boolean isDuplicate(AccountBalance accountBalance);

    /**
     * 根据id列表查询数据
     *
     * @return
     */
    List<AccountBalance> findByIds(Iterable<String> ids);

    /**
     * 执行
     *
     * @param id
     * @return
     */
    ApiResult execution(Long id);

    /**
     * 批量执行
     *
     * @return
     */
    ApiResult batchExecution(AccountBalanceDto accountBalanceDto);

    /**
     * 保存
     *
     * @param accountBalanceDto
     */
    void saveAccountBalance(AccountBalanceDto accountBalanceDto);

    void updateAccountBalance(AccountBalanceDto accountBalanceDto);

    /**
     * @param accountBalances
     * @return
     */
    Boolean verifyStatus(List<AccountBalance> accountBalances);
}
