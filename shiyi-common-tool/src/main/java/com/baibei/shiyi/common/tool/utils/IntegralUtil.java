package com.baibei.shiyi.common.tool.utils;

import com.baibei.shiyi.common.tool.constants.Constants;
import org.springframework.util.StringUtils;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/9/16 15:44
 * @description:
 */
public class IntegralUtil {
    /**
     * 商品、交易服务的积分类型转换为账户服务这边的积分类型
     *
     * @param tradeIntegral
     * @return
     */
    public static Long parseToAccountIntegral(String tradeIntegral) {
        if (StringUtils.isEmpty(tradeIntegral)) {
            return null;
        }
        switch (tradeIntegral) {
            case Constants.ProductIntegralType.DELIVERYINTEGRAL:
                return Long.valueOf(101);
            case Constants.ProductIntegralType.COMSUMEINTEGRAL:
                return Long.valueOf(102);
            case Constants.ProductIntegralType.YIJIABAO:
                return Long.valueOf(103);
            default:
                return null;
        }
    }
}