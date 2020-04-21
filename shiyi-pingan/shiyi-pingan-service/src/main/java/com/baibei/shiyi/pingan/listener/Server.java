package com.baibei.shiyi.pingan.listener;

import com.baibei.shiyi.pingan.service.IPABAcceptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ExecutorService;

import static java.util.concurrent.Executors.newFixedThreadPool;

/**
 *
 */
@Component
public class Server extends Thread {

    private static Logger logger = LoggerFactory.getLogger(Server.class);

    ServerSocket mSocket = null;
    boolean mStopped = false;


    @Value("${cash.threadCount:10}")
    private Integer threadCount;

    private ExecutorService executorService;

    @Value("${cash.listenerPort}")
    private Integer port;

    @Autowired
    List<IPABAcceptService> pabAcceptService;

    @Override
    public void run() {
        try {
            executorService = newFixedThreadPool(threadCount);
            logger.info("即将创建服务端ServerSocket..");
            createSocket();
            logger.info("创建服务端ServerSocket成功.");
            while (!mStopped) {
                Socket currentSocket = mSocket.accept();
                logger.info("获得客户端连接");
                MessageHandler handler = new MessageHandler(currentSocket, pabAcceptService);
                executorService.execute(handler);
            }
        } catch (Exception e) {
            logger.info("创建服务器异常，退出.");
            if (executorService != null) {
                executorService.shutdown();
            }
            e.printStackTrace();
        }
    }

    public void createSocket() throws Exception {
        logger.info("创建服务器端 ServerSocket...");
        Exception throwMe = null;
        boolean success = false;
        try {
            mSocket = new ServerSocket(port); //监听端口
            success = true;
        } catch (Exception e) {
            throwMe = e;
        }
        if (!success)
            throw throwMe;
    }

    /**
     * 停止服务
     */
    public void stopServer() {
        mStopped = true;

        try {
            closeServerSocket();
        } catch (Exception e) {
            // ignore--we're stopping.
        }
    }

    /**
     * 关闭 server socket.
     */
    void closeServerSocket() throws java.io.IOException {
        if (mSocket != null) {
            mSocket.close();
        }
    }

}
