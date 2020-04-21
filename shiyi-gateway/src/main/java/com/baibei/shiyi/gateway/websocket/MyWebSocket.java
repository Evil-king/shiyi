package com.baibei.shiyi.gateway.websocket;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baibei.shiyi.common.tool.utils.JacksonUtil;
import com.baibei.shiyi.gateway.utils.SocketConsts;
import com.baibei.shiyi.gateway.utils.SocketUtil;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import okhttp3.WebSocket;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by keegan on 06/07/2017.
 */
@ServerEndpoint(value = "/marketData")
@Component
@Slf4j
public class MyWebSocket {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    @Autowired
    private ChannelMessageCache channelMessageCache;

    @PostConstruct
    public void init() {
        log.info("init all  broadcast message thread");
        try {
            doInit();
        } catch (Exception e) {
            log.error("init error!", e);
        }
        log.info("init all  broadcast message thread  success!");
    }

    public void broadcast(String exchangeKey,String msg) {
        //兼容多交易所改造之前的客户端
        /*if(configReader.getDefaultExchange().equals(exchangeKey)) {
            SessionHandler.sendMessageToDefault(msg);
        }*/
        SessionHandler.sendMessageToExchange(exchangeKey, msg);
    }

    private void doInit() {
        startMarketPriceThread();
    }

    private void startMarketPriceThread() {

        new PushMessageThread("MarketPriceThread", WebSocketConstant.REAL_MARKET) {
            @Override
            public Map<String, JSONArray> getMessage() {
                return channelMessageCache.getQuoteData();
            }
        }.start();
    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        WebSoketSessionBinding.bindSession(session);
        log.info("有新连接加入！sessionId={},当前在线人数为: {}",session.getId(), addOnlineCount());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        WebSoketSessionBinding.destroy(session);
        log.info("有一连接关闭！sessionId={},当前在线人数为: {}",session.getId(), subOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("来自客户端的消息: message={}, sessionId={},requestUri={},queryString={}", message, session.getId(), session.getRequestURI(),session.getQueryString());
        try {
            Map<String, Object> msgMap =
                    (Map<String, Object>) JacksonUtil.decode(message, Map.class);
            //绑定accessToken
            if (msgMap != null && "BINDING_USER".equals(msgMap.get("type"))) {//绑定accessToken
                String accessToken = (String) msgMap.get("data");
                if (StringUtils.isNotEmpty(accessToken)) {

                    WebSoketSessionBinding.bindingAccessToken(accessToken,session);

                    JSONObject data = new JSONObject();
                    data.put("msg", "绑定成功");
                    data.put("code", "200");
                    String sendMsg = SocketUtil.createSocketJson(data, SocketConsts.WS_RSP_TYPE_BINDUSER);
                    SessionHandler.sendMessageToUser(accessToken, sendMsg);
                }
            }else if (msgMap != null && "BINDING_EXCHANGE".equals(msgMap.get("type"))) {//绑定交易所标示
               WebSoketSessionBinding.bindExchange((String) msgMap.get("data"),session);
            }
            //绑定商品
            if (msgMap != null && SocketConsts.WS_RSP_TYPE_BINDPRODUCT.equals(msgMap.get("type"))) {//绑定accessToken
                String productNos = (String) msgMap.get("data");
                if (StringUtils.isNotEmpty(productNos)) {

                    WebSoketSessionBinding.bindingProductNo(productNos,session);

                    JSONObject data = new JSONObject();
                    data.put("msg", "绑定成功");
                    data.put("code", "200");
                    String sendMsg = SocketUtil.createSocketJson(data, SocketConsts.WS_RSP_TYPE_BINDPRODUCT);
                    List<Session> sessions=new ArrayList<>();
                    sessions.add(session);
                    SessionHandler.sendMessage(sessions,sendMsg);
                }
            }
            //解绑商品
            if (msgMap != null && SocketConsts.WS_RSP_TYPE_UNTYINGPRODUCT.equals(msgMap.get("type"))) {//绑定accessToken
                WebSoketSessionBinding.untyingProduct(session);
                //如果data内参数不为空，则为更换绑定商品
                String productNos = (String) msgMap.get("data");
                if(StringUtils.isNotEmpty(productNos)){
                    WebSoketSessionBinding.bindingProductNo(productNos,session);
                }
                JSONObject data = new JSONObject();
                data.put("msg", "解绑成功");
                data.put("code", "200");
                String sendMsg = SocketUtil.createSocketJson(data, SocketConsts.WS_RSP_TYPE_UNTYINGPRODUCT);
                List<Session> sessions=new ArrayList<>();
                sessions.add(session);
                SessionHandler.sendMessage(sessions,sendMsg);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("【WebSocket连接】sessionId：{} 发生错误：{}", session.getId(), Throwables.getStackTraceAsString(error));
    }

    private abstract class PushMessageThread extends Thread {

        private String type;

        public PushMessageThread(String name, String type) {
            super(name);
            this.type = type;
        }

        public void run() {
            while (true) {
                try {
                    Map<String,String> dataMap = buildMsg();
                    for(String exchangeKey : dataMap.keySet()) {
                        broadcast(exchangeKey,dataMap.get(exchangeKey));
                    }
                    Thread.sleep(1000);
                } catch (Exception e) {
                    log.error("broadcast message error", e);
                }
            }
        }

        /**
         * 从缓存中获取消息
         * @return
         */
        private Map<String,String> buildMsg() {
            Map<String, String> msgMap = new HashMap<>();
            // JSONArray jsonArray = (JSONArray) getMessage();
            Map<String, JSONArray> quoteMap = (Map) getMessage();
            for (String key : quoteMap.keySet()){
                JSONArray jsonArray = quoteMap.get(key);
                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String exchangeKey = jsonObject.getString("exchangeTag");
                        JSONArray content = jsonObject.getJSONArray("productLatestQuoteDTOList");
                        if (content != null) {
                            if(msgMap.get(exchangeKey) == null) {
                                JSONObject json = new JSONObject();
                                json.put(WebSocketConstant.TYPE, this.type);
                                json.put(WebSocketConstant.DATA, content);
                                msgMap.put(exchangeKey, json.toJSONString());
                            } else {
                                String jsonStr = msgMap.get(exchangeKey);
                                JSONObject jsonObject1 = JSONObject.parseObject(jsonStr);
                                JSONArray jsonArray1 = jsonObject1.getJSONArray(WebSocketConstant.DATA);
                                jsonArray1.addAll(content);
                                msgMap.put(exchangeKey, jsonObject1.toJSONString());
                            }
                        }
                    }
                }
        }
            return msgMap;
        }

        public abstract Object getMessage();
    }

    private synchronized int getOnlineCount() {
        return onlineCount;
    }

    private synchronized int addOnlineCount() { return ++onlineCount; }

    private synchronized int subOnlineCount() {
        return --onlineCount;
    }
}
