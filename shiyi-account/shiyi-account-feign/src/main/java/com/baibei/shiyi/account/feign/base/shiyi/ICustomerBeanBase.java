package com.baibei.shiyi.account.feign.base.shiyi;

import com.baibei.shiyi.account.feign.bean.dto.ChangeCustomerBeanDto;
import com.baibei.shiyi.account.feign.bean.dto.CustomerNoDto;
import com.baibei.shiyi.account.feign.bean.vo.CustomerBeanVo;
import com.baibei.shiyi.common.tool.api.ApiResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: hyc
 * @date: 2019/10/29 17:30
 * @description:
 */
public interface ICustomerBeanBase {
    /**
     * 获取某一个用户下的各种豆的金额
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/shiyi/account/customerBean/getBalance", method = RequestMethod.POST)
    @ResponseBody
    ApiResult<CustomerBeanVo> getBalance(@RequestBody CustomerNoDto customerNoDto);

    /**
     * 修改豆可用金额
     */
    @RequestMapping(value = "/shiyi/account/customerBean/changeAmount",method = RequestMethod.POST)
    @ResponseBody
    ApiResult changeAmount(@RequestBody ChangeCustomerBeanDto changeCustomerBeanDto);

    /**
     * 每日定时释放待赋能
     */
    @RequestMapping(value = "/shiyi/account/customerBean/release",method = RequestMethod.POST)
    @ResponseBody
    ApiResult release();
}
