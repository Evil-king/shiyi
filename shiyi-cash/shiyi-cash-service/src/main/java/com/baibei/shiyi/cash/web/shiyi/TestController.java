package com.baibei.shiyi.cash.web.shiyi;

import com.baibei.shiyi.cash.feign.base.dto.OrderWithdrawDto;
import com.baibei.shiyi.cash.model.OrderWithdraw;
import com.baibei.shiyi.cash.service.IOrderWithdrawService;
import com.baibei.shiyi.common.tool.api.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Classname TestController
 * @Description TODO
 * @Date 2019/11/8 10:27
 * @Created by Administrator
 */
@Slf4j
@RestController
@RequestMapping("/test/cash")
public class TestController {
    @Autowired
    private IOrderWithdrawService orderWithdrawService;
    /**
     * 出金申请
     * @param
     * @return
     */
    @RequestMapping("/getByOrderNo")
    public ApiResult getByOrderNo(@RequestParam("orderNo") String orderNo){
        OrderWithdraw byOrderNo = orderWithdrawService.getByOrderNo(orderNo);
        System.out.println(byOrderNo);
        return ApiResult.success();
    }
}
