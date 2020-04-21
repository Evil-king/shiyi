package com.baibei.shiyi.pingan.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


/**
 * 监听平安银行的的请求,并发送消息到不同的接口
 */
@Component
public class PABListener implements ApplicationRunner {

    private final Logger logger = LoggerFactory.getLogger(PABListener.class);

    @Autowired
    private Server server;

    @Override
    public void run(ApplicationArguments args) {
        logger.info("----------------启动监管系统服务监听-------------------");
        server.start();
    }
}
