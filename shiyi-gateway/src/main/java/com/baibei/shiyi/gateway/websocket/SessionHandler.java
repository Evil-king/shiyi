package com.baibei.shiyi.gateway.websocket;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class SessionHandler {

    /**
     * 给某个用户发送消息
     *
     * @param accessToken
     * @param msg
     */
    public static boolean sendMessageToUser(String accessToken, String msg) {
        log.info("accessToken={}", accessToken);
        if (StringUtils.isEmpty(accessToken)) {
            log.warn("accessToken is empty");
            return false;
        }
        Session sessionTemp = WebSoketSessionBinding.getByAccessToken(accessToken);
        if (sessionTemp != null) {
            log.info("sendMessageToUser:[{}];" + "payload:[{}]", new Object[]{accessToken, msg});
            try {
                synchronized (sessionTemp) {
                    sessionTemp.getBasicRemote().sendText(msg);
                }
                return true;
            } catch (Exception e) {
                log.error("sendMessageToUser:" + accessToken + "==>Error:" + e.getMessage(), e);
            }
        }
        return false;
    }

    /**
     * 给绑定的交易所Websocket Client发送消息
     *
     * @param exchange
     * @param sendMsg
     */
    public static void sendMessageToExchange(String exchange, String sendMsg) {
        List<Session> sessionList = WebSoketSessionBinding.getByExchange(exchange);

        sendMessage(sessionList, sendMsg);
    }

    /**
     * 发送websocket 消息给默认的客户端
     *
     * @param sendMsg
     */
    public static void sendMessageToDefault(String sendMsg) {
        List<Session> sessionList = WebSoketSessionBinding.getDefaultSession();
        sendMessage(sessionList, sendMsg);
    }

    public static void sendMessage(List<Session> sessionList, String sendMsg) {
        if (sendMsg != null) {
            // log.info("推送消息, msg:{}, sessionList:{}", sendMsg, sessionList);
            for (Session session : sessionList) {
                try {
                    synchronized (session) {
                        session.getBasicRemote().sendText(sendMsg);
                    }
                } catch (IOException e) {
                    log.error("推送消息失败, msg:{}, error:" + e.getMessage(), sendMsg, e);
                } catch (Exception e1) {
                    log.error("推送消息失败, msg:{}, error:" + e1.getMessage(), sendMsg, e1);
                }
            }
        }
    }
}
