package com.baibei.shiyi.admin.modules.account.service;

import com.baibei.shiyi.admin.modules.account.bean.dto.AccountBalanceErrorDto;
import com.baibei.shiyi.admin.modules.account.bean.vo.AccountBalanceErrorVo;
import com.baibei.shiyi.admin.modules.account.model.AccountBalanceError;
import com.baibei.shiyi.common.core.mybatis.Service;
import com.baibei.shiyi.common.tool.page.MyPageInfo;


/**
 * @author: uqing
 * @date: 2019/11/29 11:22:11
 * @description: accountBalanceError服务接口
 */
public interface IAccountBalanceErrorService extends Service<AccountBalanceError> {

    MyPageInfo<AccountBalanceErrorVo> findPageList(AccountBalanceErrorDto accountBalanceError);
}
