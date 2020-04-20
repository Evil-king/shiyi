package com.baibei.shiyi.cash.dao;

import com.baibei.shiyi.cash.model.SignInBack;
import com.baibei.shiyi.common.core.mybatis.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

public interface SignInBackMapper extends MyMapper<SignInBack> {

    /**
     * 查询今天是否签到或者签退
     *
     * @param signInBackStatus
     * @param today
     * @return
     */
    List<SignInBack> findSignInBackByToday(@Param("signInBackStatus") String signInBackStatus, @Param("today") LocalDate today);
}