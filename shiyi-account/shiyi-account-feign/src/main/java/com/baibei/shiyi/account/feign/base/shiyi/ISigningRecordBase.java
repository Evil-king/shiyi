package com.baibei.shiyi.account.feign.base.shiyi;

import com.baibei.shiyi.common.tool.api.ApiResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: hyc
 * @date: 2019/11/7 10:18
 * @description:
 */
public interface ISigningRecordBase {
    @RequestMapping("/shiyi/account/signingRecord/getBalance")
    @ResponseBody
    ApiResult<String> getBalance(@RequestParam("message")  String message);
}
