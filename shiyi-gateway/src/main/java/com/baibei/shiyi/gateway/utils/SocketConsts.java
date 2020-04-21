package com.baibei.shiyi.gateway.utils;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 
* @author      作者 : lzp
* @version     创建时间：2016年5月24日 下午7:43:07 
* @description 说明：socket常量
 */

public class SocketConsts {

	/**建仓时,页面发送一条确认通知*/
	public static final String SOCKET_KEY = "SOCKET"; 
	
	/**建仓时,页面发送一条确认通知*/
	public static final int CLIENT_SEND_CREATE = 101; 
	
	/**平仓时,页面发送一条确认通知*/
	public static final int CLIENT_SEND_CLOSE = 201;
	
	/**提交建仓后,发送服务器响应通知*/
	public static final String CLIENT_SUBMIT_OPEN = "SUBMIT_OPEN"; 
	
	/**提交平仓后,发送服务器响应通知*/
	public static final String CLIENT_SUBMIT_CLOSE = "SUBMIT_CLOSE";
	
    /** 建仓响应 */
    public static final String WS_RSP_TYPE_OPEN = "WS_RSP_TYPE_OPEN";
	/** 游戏模块响应 */
	public static final String WS_RSP_TYPE_GAME = "WS_RSP_TYPE_GAME";

    /** 平仓响应 */
    public static final String WS_RSP_TYPE_CLOSE = "WS_RSP_TYPE_CLOSE";

    public static final String WS_RSP_TYPE_BINDUSER = "WS_RSP_TYPE_BINDUSER";
	/**
	 * 绑定商品
	 */
	public static final String WS_RSP_TYPE_BINDPRODUCT = "WS_RSP_TYPE_BINDPRODUCT";
	/**
	 * 解绑商品
	 */
	public static final String WS_RSP_TYPE_UNTYINGPRODUCT = "WS_RSP_TYPE_UNTYINGPRODUCT";
	
	/**在系统平仓时，发送确认信的回复类型*/
	public static final String PUSHTYPE_CLIENT_SYSC_ = "sysc"; 
	
	/**推送首页盈亏消息，发送确认信的回复类型*/
	public static final String PUSHTYPE_CLIENT_PL_ = "pl"; 
	
	/**建仓成功消息*/
	public static final String ORDER_CREATE_SUCCESS = "建仓成功";
	/**建仓成功提示码*/
    public static final String CODE_CREATE_SUCCESS = "CODE_CREATE_SUCCESS";
	/**游戏模块成功提示码*/
	public static final String CODE_GAME_SUCCESS = "CODE_GAME_SUCCESS";
	/**游戏模块失败提示码*/
	public static final String CODE_GAME_FAIL = "CODE_GAME_FAIL";
	/**建仓失败消息标识*/
	public static final String ORDER_CREATE_FAIL = "建仓失败"; 
	/**建仓失败提示码*/
    public static final String CODE_CREATE_FAIL = "CODE_CREATE_FAIL";
	
	/**平仓成功消息标识*/
	public static final String ORDER_CLOSE_SUCCESS = "平仓成功"; 
	/**平仓成功提示码*/
    public static final String CODE_CLOSE_SUCCESS = "CODE_CLOSE_SUCCESS";
	
	/**平仓失败消息标识*/
	public static final String ORDER_CLOSE_FAIL = "平仓失败"; 
	/**平仓失败提示码*/
    public static final String CODE_CLOSE_FAIL = "CODE_CLOSE_FAIL";
	
	public static final String PUSHTYPE_QUOTE = "quote"; //推送消息类型：行情
	
	public static final String PUSHTYPE_TRAND = "trand"; //推送消息类型：行情
	
	public static final String PUSHTYPE_NOFRAME = "nofram_n"; //推送消息类型：非农
	
	public static final String PUSHTYPE_EIA = "eia_n"; //推送消息类型：EIA
	
	
	
	public static ConcurrentHashMap<String, Object> currentQuotes = new ConcurrentHashMap<String, Object>();
}
