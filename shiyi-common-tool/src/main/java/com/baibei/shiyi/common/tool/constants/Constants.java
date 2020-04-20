package com.baibei.shiyi.common.tool.constants;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/4/8 10:20 AM
 * @description:
 */
public final class Constants {
    private Constants() {
    }


    /**
     * 是否有效
     */
    public interface Flag {
        // 有效
        String VALID = "1";
        // 无效
        String UNVALID = "0";
    }

    /**
     * 设备
     */
    public interface Platform {
        String Android = "Android";
        String IOS = "IOS";
        String PC = "PC";
        String H5 = "H5";
    }

    /**
     * 常用日期类型
     */
    public interface DATE {
        String YYYYMMDD = "yyyyMMdd";
        String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
        String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
        String YYYY_MM_DD = "yyyy-MM-dd";
    }

    /**
     * 挂摘牌交易方向
     */
    public interface TradeDirection {
        // 买入
        String BUY = "buy";
        // 卖出
        String SELL = "sell";
    }

    /**
     * 身份 personal=个人 enterprise=企业
     */
    public interface Identity {
        String PERSONAL = "personal";
        String ENTERPRISE = "enterprise";
    }


    /**
     * 积分类型
     */
    public interface IntegralType {
        //提货积分
        Long DELIVERY_INTEGRAL = 101L;
        //消费积分
        Long CONSUMPTION_INTEGRAL = 102L;
        //屹家保积分
        Long PRODUCT_INTEGRAL = 103L;
    }

    /**
     * 状态
     */
    public interface TransactionStatus {
        // 初始化
        String INIT = "init";
        // 成功
        String SUCCESS = "success";
        // 失败
        String FAIL = "fail";
    }

    /**
     * 上市商品交易状态
     * 交易状态，submit=已提交（创建完还未审核通过）；wait=待上市；trading=交易中；stop=停盘；exit=退市
     */
    public interface ProductMarketTradeStatus {
        String SUBMIT = "submit";
        String WAIT = "wait";
        String TRADING = "trading";
        String STOP = "stop";
        String EXIT = "exit";
    }

    /**
     * 开启关闭状态
     */
    public interface OpenStatus {
        String OPEN = "open";
        String CLOSE = "close";
    }

    /**
     * 商品组类型（common=普通类型，hot=热门推荐，new=新品上市）
     */
    public interface GroupType {
        String HOT = "hot";
        String NEW = "new";
        String COMMON = "common";
    }

    /**
     * 前端类目显示与否
     */
    public interface CategoryStatus {
        String SHOW = "show";
        String HIIDEN = "hidden";
    }

    /**
     * 前端类目是否是末端类目
     */
    public interface CategoryEnd {
        String ISEND = "1";
        String NOTEND = "0";
    }

    /**
     * 产品上下架状态
     */
    public interface ShelfStatus {
        String SHELF = "shelf";
        String UNSHELF = "unshelf";
    }

    /**
     * 积分抵扣类型.bean=积分
     */
    public interface Unit {
        String PERCENT = "percent";
        String RMB = "rmb";
        String BEAN = "bean";
    }

    /**
     * 积分类型（deliveryintegral=提货积分，comsumeintegral=消费积分，yijiabao=屹家保）
     */
    public interface ProductIntegralType {
        Map<String, String> IntegralTypeMap = new ConcurrentHashMap<>();
        String DELIVERYINTEGRAL = "deliveryintegral";
        String COMSUMEINTEGRAL = "comsumeintegral";
        String YIJIABAO = "yijiabao";

        static Map<String, String> getMapTypeText() {
            if (IntegralTypeMap.get(DELIVERYINTEGRAL) == null) {
                IntegralTypeMap.put(DELIVERYINTEGRAL, "提货积分");
            }
            if (IntegralTypeMap.get(COMSUMEINTEGRAL) == null) {
                IntegralTypeMap.put(COMSUMEINTEGRAL, "消费积分");
            }
            if (IntegralTypeMap.get(YIJIABAO) == null) {
                IntegralTypeMap.put(YIJIABAO, "屹家保");
            }
            return IntegralTypeMap;
        }
    }

    /**
     * 商品来源。（integralmodule=积分舱，firstmodule=头等舱，upmodule=升仓，extendmodule=传承仓，sharemodule=共享仓）
     */
    public interface ProductSource {
        Map<String, String> SourceMap = new ConcurrentHashMap<>();
        String INTEGRALMODULE = "integralmodule";
        String FIRSTMODULE = "firstmodule";
        String UPMODULE = "upmodule";
        String EXTENDMODULE = "extendmodule";
        String SHAREMODULE = "sharemodule";

        static Map<String, String> getMapSourceText() {
            if (SourceMap.get(INTEGRALMODULE) == null) {
                SourceMap.put(INTEGRALMODULE, "积分仓");
            }
            if (SourceMap.get(FIRSTMODULE) == null) {
                SourceMap.put(FIRSTMODULE, "头等舱");
            }
            if (SourceMap.get(UPMODULE) == null) {
                SourceMap.put(UPMODULE, "升仓");
            }
            if (SourceMap.get(EXTENDMODULE) == null) {
                SourceMap.put(EXTENDMODULE, "传承仓");
            }
            if (SourceMap.get(SHAREMODULE) == null) {
                SourceMap.put(SHAREMODULE, "共享仓");
            }
            return SourceMap;
        }
    }

    /**
     * 公告类型
     * 类型(mall:商场公告，trade:交易公告
     */
    public interface publicNoticeType {
        String MAIL = "mail";
        String TRADE = "trade";
    }

    /**
     * 订单状态，init=订单初始化；wait=待支付；undelivery=待发货（已支付）；pay_fail=支付失败；deliveryed=已发货；cancel=已取消；completed=已完成；synced=同步售后订单
     */
    public interface MallOrderStatus {
        String INIT = "init";
        String WAIT = "wait";
        String UNDELIVERY = "undelivery";
        String PAY_FAIL = "pay_fail";
        String DELIVERYED = "deliveryed";
        String CANCEL = "cancel";
        String COMPLETED = "completed";
        String SYNCED = "synced";
    }

    /**
     * 支付方式，balance=余额支付
     */
    public interface MallOrderPayWay {
        String BALANCE = "balance";
    }

    /**
     * 订单状态，cancel=已取消；undelivery=待发货；deliveryed=已发货；completed=已完成；apply_refund=退款申请；refunded=已退款
     */
    public interface MallOrderItemStatus {
        String CANCEL = "cancel";
        String UNDELIVERY = "undelivery";
        String DELIVERYED = "deliveryed";
        String COMPLETED = "completed";
        String APPLY_REFUND = "apply_refund";
        String REFUNDED = "refunded";

    }


    public interface DetuchStockStatus {
        String SUCCESS = "success";
        String FAIL = "fail";
    }

    public interface Retype {
        String IN = "in";
        String OUT = "out";
    }

    /**
     * 上下架
     */
    public interface UpDown {
        String UP = "up";
        String DOWN = "down";
    }

    /**
     * 订单退款状态，apply_refund=退款申请；refunded=已退款；reject_refund=退款驳回
     */
    public interface RefundStatus {
        Map<String, String> sourceTypeMap = new ConcurrentHashMap<>();
        String APPLY_REFUND = "apply_refund";   //申请退款
        String REFUNDED = "refunded";
        String REJECT_REFUND = "reject_refund";

        static Map<String, String> getMapTypeText() {
            if (sourceTypeMap.get(APPLY_REFUND) == null) {
                sourceTypeMap.put(APPLY_REFUND, "申请退款");
            }
            if (sourceTypeMap.get(REFUNDED) == null) {
                sourceTypeMap.put(REFUNDED, "已退款");
            }
            if (sourceTypeMap.get(REJECT_REFUND) == null) {
                sourceTypeMap.put(REJECT_REFUND, "退款驳回");
            }
            return sourceTypeMap;
        }
    }

    /**
     * 参数类型（date:日期类型，text:文本类型，select:下拉框类型，single:单选类型，area：地区控件）
     */
    public interface parameterKeyType {
        String date = "date";
        String text = "text";
        String select = "select";
        String single = "single";
        String area = "area";
        String all = date + text + select + single + area;
    }

    /**
     * （integralmodule=积分舱，firstmodule=头等舱，upmodule=升仓，extendmodule=传承仓，sharemodule=共享仓）
     */
    public interface SourceType {
        Map<String, String> sourceTypeMap = new ConcurrentHashMap<>();
        String INTEGRAL_MODULE = "integralmodule";
        String FIRST_MODULE = "firstmodule";
        String UP_MODULE = "upmodule";
        String EXTEND_MODULE = "extendmodule";
        String SHARE_MODULE = "sharemodule";

        static Map<String, String> getMapTypeText() {
            if (sourceTypeMap.get(INTEGRAL_MODULE) == null) {
                sourceTypeMap.put(INTEGRAL_MODULE, "积分舱");
            }
            if (sourceTypeMap.get(FIRST_MODULE) == null) {
                sourceTypeMap.put(FIRST_MODULE, "头等舱");
            }
            if (sourceTypeMap.get(UP_MODULE) == null) {
                sourceTypeMap.put(UP_MODULE, "升仓");
            }
            if (sourceTypeMap.get(EXTEND_MODULE) == null) {
                sourceTypeMap.put(EXTEND_MODULE, "传承仓");
            }
            if (sourceTypeMap.get(SHARE_MODULE) == null) {
                sourceTypeMap.put(SHARE_MODULE, "共享舱");
            }
            return sourceTypeMap;
        }
    }

    /**
     * 成功或者失败
     */
    public interface SuccessOrFail {
        String SUCCESS = "success";
        String FAIL = "fail";
    }

    /**
     * 商品上下架状态
     */
    public interface ProductShelfStatus {
        String SHELF = "shelf";
        String UNSHELF = "unshelf";
    }

    /**
     * 订单退款审核状态
     */
    public interface OrderRefundReviewStatus {
        String WAIT = "wait";
        String PASS = "pass";
        String REJECT = "reject";
    }

    /**
     * 更改库存类型。trade=交易；sys=系统 ;init=初始化
     */
    public interface ProductStockChangeType {
        String TRADE = "trade";
        String SYS = "sys";
        String INIT = "init";
    }

    /**
     * 是否可编辑或删除标识.allow=允许；unallow=不允许
     */
    public interface EditFlag {
        String ALLOW = "allow";
        String UNALLOW = "unallow";
    }

    /**
     * 商品有无属性标识。have=有。notHave=没有
     */
    public interface HasSkuFlag {
        String HAVE = "have";
        String NOTHAVE = "notHave";
    }

    /**
     * 菜单类型，button 按钮,menu 菜单,directory 目录
     */
    public interface MenuType {
        String BUTTON = "button";
        String MENU = "menu";
        String DIRECTORY = "directory";
    }

    /**
     * group 角色组,one:角色
     */
    public interface RoleType {
        String GROUP = "group";
        String ONE = "one";
    }

    /**
     * 机构类型
     * platform：平台；business：事业部；organization：机构；cityAgent：市代理；areaAgent：区代理；ordinaryAgent：普通代理；branchOffice:分公司
     */
    public interface OrganizationType {
        String PLATFORM = "platform";
        String BUSINESS = "business";
        String ORGANIZATION = "organization";
        String CITYAGENT = "cityAgent";
        String AREAAGENT = "areaAgent";
        String ORDINARYAGENT = "ordinaryAgent";
        String BRANCHOFFICE = "branchOffice";
    }

    /**
     * 机构状态
     */
    public interface OrganizationStatus {
        String ENABLE = "enable"; //启用
        String DISABLE = "disable"; //禁用
    }

    /**
     * 商业类型
     */
    public interface BusinessType {
        String PERSONAL = "personal";
        String ENTERPRISE = "enterprise";
    }

    /**
     * 上架商品类型（send_integral=赠送积分商品；consume_ingtegral=消费积分商品；transfer_product=交割商品）
     */
    public interface ShelfType {
        String SEND_INTEGRAL = "send_integral";
        String CONSUME_INGTEGRAL = "consume_integral";
        String TRANSFER_PRODUCT = "transfer_product";
    }

    /**
     * 类型(money:资金 consumption:消费 exchange:兑换，shiyi:屹家无忧，mallAccount:商城账户)
     */
    public interface BeanType {
        String MONEY = "money";
        String CONSUMPTION = "consumption";
        String EXCHANGE = "exchange";
        String SHIYI = "shiyi";
        String MALLACCOUNT = "mallAccount";
    }

    /**
     * 支付方式
     */
    public interface PayWay {
        String MONEY = "money";   // 现金支付
        String CONSUMPTION = "consumption"; // 消费积分支付
    }

    /**
     * 出金订单状态(1、初始化 1、提现申请中 2、提现审核通过(后台操作的) 3、渠道处理中 4、提现成功 5、提现失败、6、审核不通过、7.系统处理中)'
     */
    public interface OrderWithdrawStatus {
        String WITHDRAW_INIT = "0";
        String WITHDRAW_APLLYING = "1";
        String WITHDRAW_PASS = "2";
        String WITHDRAW_DOING = "3";
        String WITHDRAW_SUCCESS = "4";
        String WITHDRAW_FAIL = "5";
        String WITHDRAW_UNPASS = "6";
        String WITHDRAW_SYS_DOING = "7";

    }

    /**
     * 出金类型(1、交易网发起的 2、银行发起的 3、调账)
     */
    public interface WithdrawOrderType {
        String COMPANY = "1";
        String BANK = "2";
        String TRANSFER = "3";
    }

    /**
     * 签约状态
     */
    public interface SigningStatus {
        String SIGNING_CREATE = "1"; //创建
        String SIGNING_UPDATE = "2"; // 修改
        String SIGNING_DELETE = "3"; //删除
    }

    /**
     * 清算状态
     */
    public interface CleanStatus {
        String WAIT = "wait";
        String SUCCESS = "success";
        String FAIL = "fail";
    }

    /**
     * 业务办理返还类型
     */
    public interface AmountReturnType {
        String FEE = "fee"; // 手续费
    }

    /**
     * 清算操作状态
     */
    public interface CleanOperateStatus {
        String WAIT = "wait";
        String COMPLETED = "completed";
    }

    /**
     * 清算消息profitAmount = 总盈利；lossAmount=总亏损；withdrawNewBalance=出金影响newBalance字段值；depositNewBalance=入金影响newBalance字段值
     */
    public interface SettlementEffectField {
        String PROFITAMOUNT = "profitAmount";
        String LOSSAMOUNT = "lossAmount";
        String WITHDRAW_NEWBALANCE = "withdrawNewBalance";
        String DEPOSIT_NEWBALANCE = "depositNewBalance";
    }

    /**
     * 对账差异类型，long_diff=长款差错（银行有，系统没有），short_diff=短款差错（系统有，银行没有），amount_diff=金额不一致，status_diff=状态不一致,amount_status_diff=金额和状态都不一致
     */
    public interface DiffType {
        String LONG_DIFF = "long_diff";
        String SHORT_DIFF = "short_diff";
        String AMOUNT_DIFF = "amount_diff";
        String STATUS_DIFF = "status_diff";
        String AMOUNT_STATUS_DIFF = "amount_status_diff";
    }

    public interface Status {
        String SUCCESS = "success";
        String FAIL = "fail";
        String WAIT = "wait";
        String TIMEOUT = "timeout";
    }

    /**
     * 差异流水状态，wait=待处理，deal=已处理，doing=正在处理
     */
    public interface DiffStatus {
        String WAIT = "wait";
        String DEAL = "deal";
        String DOING = "doing";
    }

    /**
     * 执行内容编码，sign_in=签到，sign_back=签退，
     * accountcheck=发起出入金对账，accountcheck_file=获取出入金对账文件，amount_return=生成业务返还数据，
     * clean_file=生成清算文件，launch_clean=发起清算，clean_process=查看清算进度
     */
    public interface CleanFlowPathCode {
        String SIGN_IN = "sign_in";
        String SIGN_BACK = "sign_back";
        String ACCOUNTCHECK = "accountcheck";
        String ACCOUNTCHECK_FILE = "accountcheck_file";
        String AMOUNT_RETURN = "amount_return";
        String CLEAN_FILE = "clean_file";
        String LAUNCH_CLEAN = "launch_clean";
        String CLEAN_PROCESS = "clean_process";
    }

    /**
     * 清算流程状态
     */
    public interface CleanFlowPathStatus {
        String WAIT = "wait";
        String COMPLETED = "completed";
    }

    /**
     * 清算日志状态
     */
    public interface CleanLogStatus {
        String WAIT = "wait";
        String SUCCESS = "success";
        String FAIL = "fail";
    }

    /**
     * 通证提取订单状态（wait：待审核 success：审核通过 fail:驳回）
     */
    public interface ExtractOrderStatus {
        String WAIT = "wait";
        String SUCCESS = "success";
        String FAIL = "fail";
    }

    /**
     * 台账状态 doing=正在处理中；finished=已完成台账;undo=未进行台账操作(未台账)；fail=失败
     */
    public interface Apply1010Status {
        String DOING = "doing";
        String FINISHED = "finished";
        String UNDO = "undo";
        String FAIL = "fail";
    }

    /**
     * SettlementMetadataMsg清算消息业务类型
     */
    public interface TransferBizType {
        String TRADE = "0";
        String COMMISSION = "7";
        String FEE = "1";
    }

    /**
     * 清算状态
     */
    public interface SettlementDataStatus {
        String WAIT = "wait";   // 待清算
        String SUCCESS = "success"; // 清算成功
        String FAIL = "fail";   // 清算失败
        String NOTHING = "nothing"; // 不清算
    }

    /**
     * 交易商品状态
     */
    public interface  TradeProductStatus {
        String WAIT  = "wait";//待上市
        String TRADING = "trading"; //交易中
        String EXIT = "exit"; //退市
    }


    public interface TradeProductPicType {
        String SLIDE = "slide"; //幻灯片
        String DETAILS = "details"; //详情图
    }

    /**
     * tradeBuy：交易（摘他人的买单）
     * tradeSell：交易（摘他人的卖单）
     * hangBuy：挂买单
     * hangSell：挂卖单
     * revokeBuy:撤销挂买的单
     * revokeSell:撤销挂卖的单
     */
    public interface QuotationOperateType {
        String TRADE_BUY = "tradeBuy";
        String TRADE_SELL = "tradeSell";
        String HANG_BUY = "hangBuy";
        String HANG_SELL = "hangSell";
        String REVOKE_BUY = "revokeBuy";
        String REVOKE_SELL = "revokeSell";
    }

    /**
     * 行情档位类型。
     * hangBuy=挂买档位
     * hangSell=挂卖档位
     */
    public interface QuotationPosition {
        String HANG_BUY = "hangBuy";
        String HANG_SELL = "hangSell";
    }

}

