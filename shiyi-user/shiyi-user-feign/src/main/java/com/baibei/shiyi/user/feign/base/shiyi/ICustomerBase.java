package com.baibei.shiyi.user.feign.base.shiyi;

import com.baibei.shiyi.account.feign.bean.dto.CustomerNoDto;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.user.feign.bean.vo.CustomerVo;
import com.baibei.shiyi.user.feign.bean.vo.PABCustomerVo;
import feign.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: hyc
 * @date: 2019/5/2414:32
 * @description:
 */
public interface ICustomerBase {
    /**
     * 通过交易商编码查询用户信息
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/shiyi/user/find", method = RequestMethod.POST)
    @ResponseBody
    ApiResult<CustomerVo> findUserByCustomerNo(@RequestBody CustomerNoDto customerNoDto);


    /**
     * 通过手机号查找到交易商编码
     *
     * @param mobile
     * @return
     */
    @RequestMapping(value = "/shiyi/user/findByMobile")
    @ResponseBody
    ApiResult<String> findCustomerNoByMobile(@RequestParam("mobile") String mobile);

    @RequestMapping(value="/shiyi/user/findByCustomerNo")
    @ResponseBody
    ApiResult<PABCustomerVo> findByCustomerNo (@RequestParam("customerNo")String customerNo);

}
