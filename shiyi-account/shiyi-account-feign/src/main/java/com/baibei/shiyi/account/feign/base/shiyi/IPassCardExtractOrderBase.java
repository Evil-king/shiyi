package com.baibei.shiyi.account.feign.base.shiyi;

import com.baibei.shiyi.account.feign.bean.dto.CustomerNoDto;
import com.baibei.shiyi.account.feign.bean.vo.CustomerBeanVo;
import com.baibei.shiyi.common.tool.api.ApiResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: hyc
 * @date: 2020/1/3 11:12
 * @description:
 */
public interface IPassCardExtractOrderBase {
    /**
     * 系统执行自动审核提取仓单
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/shiyi/account/passCardExtractOrder/systemOperation", method = RequestMethod.POST)
    @ResponseBody
    ApiResult systemOperation();
}
