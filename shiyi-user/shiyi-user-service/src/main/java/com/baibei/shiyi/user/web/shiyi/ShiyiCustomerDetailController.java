package com.baibei.shiyi.user.web.shiyi;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.user.feign.base.shiyi.ICustomerDetailBase;
import com.baibei.shiyi.user.feign.bean.vo.CustomerVo;
import com.baibei.shiyi.user.feign.bean.vo.RealnameInfoVo;
import com.baibei.shiyi.user.service.ICustomerDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: hyc
 * @date: 2019/12/3 19:52
 * @description:
 */
@RestController
public class ShiyiCustomerDetailController implements ICustomerDetailBase {
    @Autowired
    private ICustomerDetailService customerDetailService;
    @Override
    public ApiResult<RealnameInfoVo> findRealNameByCustomerNo(@RequestParam("customerNo") String customerNo) {
        return ApiResult.success(customerDetailService.getRealnameInfo(customerNo));
    }
}
