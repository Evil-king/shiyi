package com.baibei.shiyi.admin.modules.account.async;

import com.alibaba.fastjson.JSONObject;
import com.baibei.shiyi.admin.modules.account.dao.AccountBalanceMapper;
import com.baibei.shiyi.admin.modules.account.model.AccountBalance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AccountBalanceAsync {

    @Autowired
    private AccountBalanceMapper accountBalanceMapper;

    /**
     * 修改线下出入金账号余额的状态异步任务
     */
    @Async
    public void updateAccountBalanceStatus(AccountBalance accountBalance) {
        if (log.isDebugEnabled()) {
            log.debug("修改当前余额数据为{}", JSONObject.toJSONString(accountBalance));
        }
        accountBalanceMapper.updateByPrimaryKeySelective(accountBalance);
    }
}
