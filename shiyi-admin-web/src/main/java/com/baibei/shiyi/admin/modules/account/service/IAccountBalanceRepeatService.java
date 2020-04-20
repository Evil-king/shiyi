package com.baibei.shiyi.admin.modules.account.service;

import com.baibei.shiyi.admin.modules.account.bean.dto.AccountBalanceRepeatDto;
import com.baibei.shiyi.admin.modules.account.bean.vo.AccountBalanceRepeatVo;
import com.baibei.shiyi.admin.modules.account.model.AccountBalanceRepeat;
import com.baibei.shiyi.common.core.mybatis.Service;
import com.baibei.shiyi.common.tool.page.MyPageInfo;

import java.util.List;


/**
 * @author: uqing
 * @date: 2019/12/02 22:57:52
 * @description: AccountBalanceRepeat服务接口
 */
public interface IAccountBalanceRepeatService extends Service<AccountBalanceRepeat> {

    MyPageInfo<AccountBalanceRepeatVo> findPageList(AccountBalanceRepeatDto accountBalanceRepeat);
}
