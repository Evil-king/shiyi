package com.baibei.shiyi.gateway.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baibei.shiyi.gateway.dto.MarketDataVo;
import com.baibei.shiyi.gateway.websocket.MyWebSocket;
import com.baibei.shiyi.gateway.websocket.SessionHandler;
import com.baibei.shiyi.gateway.websocket.WebSocketConstant;
import com.baibei.shiyi.gateway.websocket.WebSoketSessionBinding;
import com.baibei.shiyi.quotation.feign.bean.dto.PubQuoteDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/6/21 6:19 PM
 * @description: Redis订阅消息接收器
 */


@Component
@Slf4j
public class RedisMessageReceiver {

/**
     * 行情监听
     * @param msg
     */

    public void marketData(String msg) {
//        log.info("行情监听 msg={}", msg);
        try {
            String encode = StringEscapeUtils.unescapeJava(msg);
            StringBuffer buffer = new StringBuffer(encode);
            buffer.replace(0,1,"");
            buffer.replace(encode.length()-2,encode.length(),"");
            PubQuoteDto PubQuoteDto = JSONObject.parseObject(buffer.toString(), PubQuoteDto.class);
            MarketDataVo marketDataVo=new MarketDataVo();
            marketDataVo.setType(WebSocketConstant.REAL_MARKET);
            marketDataVo.setData(JSON.toJSONString(PubQuoteDto));
            //获取所有绑定好的商品交易编码，
            ConcurrentHashMap<String, String> productMap = WebSoketSessionBinding.getProductMap();
            List<Session> sessions=new ArrayList<>();
            for(String key : productMap.keySet()) {
                if(productMap.get(key).indexOf(PubQuoteDto.getProductTradeNo())!=-1){
                    Session session=WebSoketSessionBinding.getSession(key);
                    sessions.add(session);
                }
            }
            SessionHandler.sendMessage(sessions,JSON.toJSONString(marketDataVo));
//            log.info("行情监听 msg={}",JSON.toJSONString(marketDataVo));
//            SessionHandler.sendMessageToDefault(JSON.toJSONString(marketDataVo));
        }catch (Exception e) {
            log.error("行情监听",e);
        }
    }


}

