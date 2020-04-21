package com.baibei.shiyi.gateway.utils;

import com.alibaba.fastjson.JSONObject;
import com.baibei.shiyi.gateway.dto.PushMessage;

/**
 * 
* @author      作者 : lzp
* @version     创建时间：2016年7月18日 上午10:59:18 
* @description 说明：socket工具类
 */
public class SocketUtil {
	
	/**
	 * 创建socket推送消息，以json格式返回
	 * @param data	推送的消息内容
	 * @param type	推送的类型
	 */
    public static String createSocketJson(JSONObject data, String type) {
    	PushMessage<JSONObject> pushMsg = new PushMessage<JSONObject>();
		pushMsg.setData(data);
		pushMsg.setType(type);
		String tempJson = JSONObject.toJSONString(pushMsg);
        return tempJson;
    }
	
}
