package com.baibei.shiyi.order.web.api;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.utils.BeanUtil;
import com.baibei.shiyi.order.common.vo.PayChannelVo;
import com.baibei.shiyi.order.model.PayChannel;
import com.baibei.shiyi.order.service.IPayChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/8/12 17:44
 * @description:
 */
@RestController
@RequestMapping("/api/order")
public class ApiOrderController {
    @Value("${order.cancel.reason}")
    private String cancelReason;
    @Autowired
    private IPayChannelService payChannelService;


    /**
     * 支付渠道列表
     *
     * @return
     */
    @PostMapping("/payChannel")
    public ApiResult payChannel() {
        List<PayChannel> list = payChannelService.getUpPayChannelList();
        if (CollectionUtils.isEmpty(list)) {
            return ApiResult.successWithEmptyList();
        }
        return ApiResult.success(BeanUtil.copyProperties(list, PayChannelVo.class));
    }

    /**
     * 订单取消原因列表
     *
     * @return
     */
    @PostMapping("/cancelReason")
    public ApiResult<List<String>> cancelReason() {
        return ApiResult.success(Arrays.asList(cancelReason.split(",")));
    }
}