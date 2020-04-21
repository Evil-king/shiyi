package com.baibei.shiyi.gateway.websocket;

/**
 * Created by keegan on 07/07/2017.
 */
public class WebSocketConstant {
    // 协议格式
    public static final String TYPE = "type";
    public static final String DATA = "data";

    // 协议类型
    public static final String CONNECT_SUCCESS = "CONNECT_SUCCESS"; // 连接成功
    public static final String REAL_MARKET = "REAL_MARKET";// 实时行情
    public static final String MARKET_STATUS = "MARKET_STATUS"; // 市场开休市状态
    public static final String PRODUCT_STATUS = "PRODUCT_STATUS"; // 商品开休市状态
    public static final String TOP_ORDER = "TOP_ORDER"; // 商品买卖档位
}
