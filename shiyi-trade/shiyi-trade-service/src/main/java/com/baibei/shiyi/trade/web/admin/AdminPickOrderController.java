package com.baibei.shiyi.trade.web.admin;


import com.baibei.shiyi.common.tool.api.ApiResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 后台提货订单服务
 */
@RestController
@RequestMapping("/admin/pickOrder")
public class AdminPickOrderController {


    /**
     * 提货订单驳回
     *
     * @param customerNO 客户编号
     * @return
     */
    @PostMapping("/pickOrderCancel")
    public ApiResult<String> pickOrderCancel(@RequestParam("customerNO") String customerNO) {
        System.out.println(customerNO);
        return ApiResult.success();
    }

    /**
     * 提货订单成功
     *
     * @return
     */
    @PostMapping("/pickOrderSuccess")
    public ApiResult<String> pickOrderSuccess() {
        return new ApiResult<>();
    }
}
