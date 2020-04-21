package com.baibei.shiyi.user.feign.base.shiyi;

import com.baibei.shiyi.account.feign.bean.dto.CustomerNoDto;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.user.feign.bean.vo.CustomerVo;
import com.baibei.shiyi.user.feign.bean.vo.RealnameInfoVo;
import org.springframework.web.bind.annotation.*;

/**
 * @author: hyc
 * @date: 2019/12/3 19:45
 * @description:
 */
public interface ICustomerDetailBase {
    /**
     * 通过交易商编码查询用户信息
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/shiyi/customerDetail/findByCustomer", method = RequestMethod.POST)
    @ResponseBody
    ApiResult<RealnameInfoVo> findRealNameByCustomerNo(@RequestParam("customerNo") String customerNo);
}
