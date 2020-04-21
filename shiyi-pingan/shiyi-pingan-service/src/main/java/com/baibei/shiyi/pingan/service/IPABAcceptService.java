package com.baibei.shiyi.pingan.service;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.enumeration.PABFunctionType;
import com.baibei.shiyi.pingan.feign.base.dto.PABAcceptDto;


/**
 * 平安银行顶级接口
 * 接受
 */
public interface IPABAcceptService {


    /**
     * 接受银行消息并执行结果
     *
     * @param response
     */
    ApiResult<String> execute(PABAcceptDto response);

    /**
     *
     */
    PABFunctionType getType();
}
