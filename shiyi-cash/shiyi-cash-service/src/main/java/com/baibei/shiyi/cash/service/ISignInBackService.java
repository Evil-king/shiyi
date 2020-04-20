package com.baibei.shiyi.cash.service;

import com.baibei.shiyi.cash.feign.base.dto.SignInBackDto;
import com.baibei.shiyi.cash.feign.base.vo.SignInBackVo;
import com.baibei.shiyi.cash.model.SignInBack;
import com.baibei.shiyi.common.core.mybatis.Service;


/**
 * @author: uqing
 * @date: 2019/11/01 10:28:13
 * @description: SignInBack服务接口
 */
public interface ISignInBackService extends Service<SignInBack> {

    /**
     * 签到或签退
     *
     * @param signInBackDto
     * @return
     */
    SignInBackVo signInOrBack(SignInBackDto signInBackDto);

    /**
     * 查询今天是否签到|签退
     *
     * @param status 签到 1 ,签退 2
     * @return true 为存在
     */
    Boolean isToDaySignInBack(String status);


    /**
     * 获取系统当前的签到状态
     *
     * @return
     */
    SignInBackDto lastSignStatus();

}
