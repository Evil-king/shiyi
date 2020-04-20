package com.baibei.shiyi.common.tool.constants;

import java.text.MessageFormat;
import java.util.UUID;

/**
 * @author: 会跳舞的机器人
 * @date: 17/12/29 上午10:34
 * @description: Redis key定义,前缀为服务名,注释写明数据结构类型
 */
public class RedisConstant {

    public static void main(String[] args) {
        String key = MessageFormat.format(USER_CUSTOMERINFO, "20190523");
        System.out.println(key);
    }

    /**
     * 客户信息缓存,{0}为customer_no
     * 数据类型:hash
     */
    public static final String USER_CUSTOMERINFO = "USERSERVICE:CUSTOMERINFO:{0}";
    /**
     * 期初资金缓存，{0}为customer_no
     * 数据类型:hash
     */
    public static final String ACCOUNT_INIT_FUND = "ACCOUNTSERVICE:ACCOUNT_INIT_FUND:{0}";
    /**
     * 挂买时的可提资金
     */
    public static final String ACCOUNT_WITHDRAW_AMOUT = "ACCOUNTSERVICE:ACCOUNT_WITHDRAW_AMOUT:{0}";
    /**
     * 用户登录失败次数，{0}为customer_no
     * 数据类型：hash
     */
    public static final String USER_ERROR_COUNT = "USERSERVICE:ERROR_COUNT:{0}";
    /**
     * 用户token存储，{0}为customer_no
     * 数据类型：hash
     */
    public static final String PREFIX_USER_TOKEN = "USERSERVICE:PREFIX_USER_TOKEN:{0}";
    /**
     *
     */
    public static final String PREFIX_TOKEN_CODE = "USERSERVICE:PREFIX_TOKEN_CODE:{0}";

    /**
     * 交易日,数据类型:String,value=1表示当前为交易时间,value=0表示当前非交易时间
     */
    public static final String TRADE_TRADE_TIME = "TRADESERVICE:TRADETIME";

    /**
     * 当前交易日
     */
    public static final String TRADE_TRADE_DAY = "TRADESERVICE:TRADEDAY:CONFIG";

    /**
     * 闭市标识，数据类型：String，values=1表示当前已闭市；value=0表示当前未闭市
     */
    public static final String TRADE_CLOSE = "TRADESERVICE:TRADE:CLOSE";

    /**
     * 闭市后发送的通知，数据类型：String，value=1表示已发送通知；value=0表示未发送通知
     */
    public static final String TRADE_CLOSE_NOTICE = "TRADESERVICE:TRADE:CLOSE:NOTICE";

    /**
     * 上市商品缓存,{0}为商品交易编码
     * 数据结构:hash
     */
    public static final String PRODUCT_TRADE_NO = "PRODUCTSERVICE:TRADENO:{0}";


    /**start:行情服务（QUOTESERVICE）**/
    /**
     * 商品最新行情 {0}为商品交易编码
     * 数据结构：String
     */
    public static final String QUOTESERVICE_LAST_MARKET_DATA = "QUOTESERVICE:LAST_MARKET_DATA:{0}";

    /**
     * 商品昨收价 {0}为商品交易编码
     * 数据结构：String
     */
    public static final String QUOTESERVICE_YESTDAT_PRICE = "QUOTESERVICE_YESTDAT_PRICE:{0}";

    /**
     * 存储每一笔的行情数据 {1}为商品交易编码
     * 数据结构sort set
     */
    public static final String QUOTESERVICE_EVERY_MARKET_DATA = "QUOTESERVICE:EVERY_MARKET_DATA:{1}";

    /**
     * k线  {0}周期段（如：ONE_MINUTE） {1}为商品交易编码
     * 数据结构sort set
     */
    public static final String QUOTESERVICE_PERIOD_MARKET_DATA = "QUOTESERVICE:{0}_MARKET_DATA:{1}";

    /**
     * 行情监听渠道
     */
    public static final String QUOTESERVICE_KLINE_CHANNEL = "QUOTESERVICE_KLINE_CHANNEL";

    /**
     * 行情分布式锁
     * {0}:商品编码
     */
    public static final String QUOTESERVICE_KLINE_LOCK_KEY = "QUOTESERVICE_KLINE_LOCK_KEY:{0}";

    /**
     * 售量统计key
     * 售量：当前系统挂出的未成交的卖单的总数量
     * {0}:商品编码
     */
    public static final String QUOTESERVICE_HANG_SELL_COUNT = "QUOTESERVICE_HANG_SELL_COUNT:{0}";

    /**
     * 购量统计key
     * 购量：当前系统挂出的未成交的买单的总数量
     * {0}:商品编码
     */
    public static final String QUOTESERVICE_HANG_BUY_COUNT = "QUOTESERVICE_HANG_BUY_COUNT:{0}";

    /**
     * 报价区（报价档位）
     * {0}:商品编码
     * {1}:方向（hangBuy=挂买，hangSell=挂卖）
     */
    public static final String QUOTESERVICE_PRICE_POSITION = "QUOTESERVICE_PRICE_POSITION:{0}:{1}";

    /**
     * 行情队列
     * {0}:商品编码
     */
    public static final String QUOTESERVICE_QUOTE_LIST = "QUOTESERVICE_QUOTE_LIST:{0}";

    /**
     * 推送行情管道
     * {0}:商品编码
     */
    public static final String QUOTESERVICE_PUB_QUOTE_CHANNEL = "QUOTESERVICE_PUB_QUOTE_CHANNEL:{0}";
    /**end:行情服务（QUOTATION）**/

    /**
     * 短信验证码时长,{0}为短信码
     */
    public static final String SMS_USER_PHONE = "PUBSMSSERVICE:USERPHONE:{0}";

    /**
     * 首页区域,{0}为区域编码+区域ID
     */
    public static final String AREA_CODE_ID = "CONTENTSERVICE:UNIQUE:{0}";

    /**
     * 首页区域,{0}ALL
     */
    public static final String AREA_INDEX = "CONTENTSERVICE:INDEX:{0}";

    /**
     * 世屹后台登录用户端的token,{0}用户名
     */
    public static final String PREFIX_SHIYI_ADMIN_USER_TOKEN = "USERSERVICE:PREFIX_SHIYI_ADMIN_USER_TOKEN:{0}";

    /**
     * 台账状态
     */
    public static final String APPLY1010_STATUS = "apply1010_status";

    /**
     * 用户当天实名验证次数
     */
    public static final String REALNAME_VERIFICATION_LIMIT = "USERSERVICE:REALNAME_VERIFICATION_LIMIT:{0}";

    /**
     * 交易参数配置
     */
    public static final String TRADE_CONFIG = "TRADE:CONFIG";

    /**
     * 委托单分布式锁key
     */
    public static final String TRADE_ENTRUST_LOCK = "TRADE:ENTRUST:{0}";


}
