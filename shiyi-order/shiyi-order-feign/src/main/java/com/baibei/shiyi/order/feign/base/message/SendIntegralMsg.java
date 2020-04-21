package com.baibei.shiyi.order.feign.base.message;

import com.baibei.shiyi.common.tool.bean.KeyValue;
import lombok.Data;

import java.util.List;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/11/6 10:35
 * @description:
 */
@Data
public class SendIntegralMsg {
    private String customerNo;
    private String orderNo;
    private List<KeyValue> integralList;
}