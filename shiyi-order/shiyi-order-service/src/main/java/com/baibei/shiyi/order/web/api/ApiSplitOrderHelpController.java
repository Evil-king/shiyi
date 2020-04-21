package com.baibei.shiyi.order.web.api;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.order.biz.SplitOrderBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/8/29 18:07
 * @description: 手动拆单
 */
@RestController
@RequestMapping("/api/order")
public class ApiSplitOrderHelpController {
    @Autowired
    private SplitOrderBiz splitOrderBiz;


    /**
     * 手动拆单
     *
     * @param orderNoList
     * @return
     */
    @GetMapping("/split")
    public ApiResult demo(@RequestParam("orderNoList") String orderNoList) {
        String[] orderNo = orderNoList.split(",");
        if (orderNo.length != 0) {
            for (int i = 0; i < orderNo.length; i++) {
                splitOrderBiz.splitOrder(orderNo[i]);
            }
        }
        return ApiResult.success();
    }
}