package com.baibei.shiyi.gateway.websocket;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import javax.websocket.Session;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p></p>
 *
 * @author zhangyue
 * @date 2017/10/25
 */
@Slf4j
public class WebSoketSessionBinding {

    /**
     * key 是 sessionId，Value是session对象
     */
    private static ConcurrentHashMap<String, Session> sessionMap = new ConcurrentHashMap<>();

    /**
     * key 是 accessToken，Value是sessionId对象
     */
    private static ConcurrentHashMap<String, String> accessTokenSessionMap = new ConcurrentHashMap<>();

    /**
     * key 是 exchange，Value是sessionId对象列表
     */
    private static ConcurrentHashMap<String, Vector<String>> exchangeSessionMap = new ConcurrentHashMap<>();
    /**
     * key是,value是商品交易编号（以逗号隔开）
     */
    private static ConcurrentHashMap<String, String> productMap = new ConcurrentHashMap<>();


    private static List<String> defaultSession = new Vector<>();

    /**
     * 绑定access token
     * @param accessToken
     * @param session
     */
    public static void bindingAccessToken(String accessToken, Session session) {
        if (StringUtils.isNotEmpty(accessToken)) {
            accessTokenSessionMap.put(accessToken,session.getId());
            log.info("accessToken:[{}]与websocket会话绑定[{}]", accessToken, session.getId());
        }
    }
    /**
     * 绑定商品交易编号
     * @param productNos
     * @param session
     */
    public static void bindingProductNo(String productNos, Session session) {
        if (StringUtils.isNotEmpty(productNos)) {
            productMap.put(session.getId(),productNos);
            log.info("productNos:[{}]与websocket会话绑定[{}]", productNos, session.getId());
        }
    }

    /**
     * 绑定exchange交易所标示
     * @param exchange
     * @param session
     */
    public static void bindExchange(String exchange, Session session) {
        if (StringUtils.isNotEmpty(exchange)) {
            exchangeSessionMap.putIfAbsent(exchange, new Vector<String>());
            exchangeSessionMap.get(exchange).add(session.getId());
            //当绑定交易所时，从默认交易所集合中remove
            defaultSession.remove(session.getId());
            log.info("exchange:[{}]与websocket会话绑定[{}]", exchange, session.getId());
        }
    }

    /**
     * 通过accessToken获取Session
     * @param accessToken
     * @return
     */
    public static Session getByAccessToken(String accessToken) {
        String sessionId = accessTokenSessionMap.get(accessToken);
        if(StringUtils.isNotEmpty(sessionId)) {
            Session session = sessionMap.get(sessionId);
            if(session != null && session.isOpen()) {
                return session;
            }else {
                accessTokenSessionMap.remove(accessToken);
            }
        }
        return null;
    }

    /**
     * 通过accessToken获取Session
     * @param exchange
     * @return
     */
    public static List<Session> getByExchange(String exchange) {
        List<String> sessionIds = exchangeSessionMap.get(exchange);
        return findSessionList(sessionIds);
    }

    /**
     * 通过seesionid获取session
     * @param sessionId
     * @return
     */
    public static Session getSession(String sessionId) {
        Session session = sessionMap.get(sessionId);
        return session;
    }
    /**
     * 销毁session
     * @param session
     */
    public static void destroy(Session session) {
        sessionMap.remove(session.getId());
        defaultSession.remove(session.getId());
        productMap.remove(session.getId());
        if (accessTokenSessionMap.containsValue(session.getId())) {
            for (String accessToken : accessTokenSessionMap.keySet()) {
                String sessionId = accessTokenSessionMap.get(accessToken);
                if (sessionId.equals(session.getId())) {
                    accessTokenSessionMap.remove(accessToken);
                    return;
                }
            }
        }
    }

    /**
     * 解绑商品
     * @param session
     */
    public static void untyingProduct(Session session) {
        productMap.remove(session.getId());
    }

    public static void bindSession(Session session) {
        sessionMap.put(session.getId(),session);
        defaultSession.add(session.getId());
    }

    public static List<Session> getDefaultSession() {
        List<String> sessionIds = defaultSession;
        return findSessionList(sessionIds);
    }

    public static List<Session> findSessionList(List<String> sessionIds) {
        List<Session> sessionList = new ArrayList<>();
        if(sessionIds != null) {
            Iterator<String> iterator = sessionIds.iterator();
            while(iterator.hasNext()){
                String sessionId = iterator.next();
                Session session = sessionMap.get(sessionId);
                if(session != null && session.isOpen()) {
                    sessionList.add(session);
                }else {
                    iterator.remove();
                }
            }
        }
        return sessionList;
    }

    public static ConcurrentHashMap<String, String> getProductMap(){
        return productMap;
    }
}
