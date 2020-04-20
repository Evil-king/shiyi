package com.baibei.shiyi.account.feign.base.admin;

import com.baibei.shiyi.account.feign.bean.dto.CustomerNoDto;
import com.baibei.shiyi.account.feign.bean.vo.CustomerBeanVo;
import com.baibei.shiyi.common.tool.api.ApiResult;
import org.springframework.web.bind.annotation.*;

/**
 * @author: hyc
 * @date: 2019/11/1 13:43
 * @description:
 */
public interface IAdminCustomerBeanBase {
    /**
     * 获取某一个用户下的各种豆的金额
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/shiyi/account/customerBean/getBeanBalance", method = RequestMethod.POST)
    @ResponseBody
    ApiResult<CustomerBeanVo> getBeanBalance(@RequestParam("customerNo") String customerNo);
}
