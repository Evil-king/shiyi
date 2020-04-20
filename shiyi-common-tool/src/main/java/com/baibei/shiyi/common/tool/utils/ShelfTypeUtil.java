package com.baibei.shiyi.common.tool.utils;

import com.baibei.shiyi.common.tool.constants.Constants;
import org.springframework.util.StringUtils;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/10/30 14:51
 * @description:
 */
public class ShelfTypeUtil {

    /**
     * 根据商品类型获取描述
     * 赠送积分商品（余额），消耗积分商品（金豆），交割商品（红豆）
     *
     * @param shelfType
     * @return
     */
    public static String getDesc(String shelfType) {
        if (StringUtils.isEmpty(shelfType)) {
            return "";
        }
        switch (shelfType) {
            case Constants.ShelfType.SEND_INTEGRAL:
                return "现金";
            case Constants.ShelfType.CONSUME_INGTEGRAL:
                return "消费积分";
            default:
                return "";
        }
    }

    /**
     * 根据商品类型获取支付方式
     *
     * @param shelfType
     * @return
     */
    public static String getPayWay(String shelfType) {
        if (StringUtils.isEmpty(shelfType)) {
            return "";
        }
        switch (shelfType) {
            case Constants.ShelfType.SEND_INTEGRAL:
                return Constants.PayWay.MONEY;
            case Constants.ShelfType.CONSUME_INGTEGRAL:
                return Constants.PayWay.CONSUMPTION;
            default:
                return "";
        }
    }


}