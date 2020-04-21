package com.baibei.shiyi.gateway.redis;/*
package com.baibei.shiyi.quotation.redis;

import com.baibei.shiyi.common.tool.constants.RedisConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/6/21 6:16 PM
 * @description: Redis订阅消息监听
 */

import com.baibei.shiyi.common.tool.constants.RedisConstant;
import com.sun.javafx.binding.StringFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import java.text.MessageFormat;
import java.util.List;

@Configuration
public class RedisMessageListener {
    @Autowired
    private RedisMessageReceiver redisMessageReceiver;

    @Value("${subscribeProductMarketNo}")
    private String subscribeProductMarketNo;


/**
     * Redis订阅
     *
     * @param connectionFactory
     * @param kLineListenerAdapter
     * @param
     * @return
     */

    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory, MessageListenerAdapter kLineListenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        String[] productNos=subscribeProductMarketNo.split(",");
        for (int i = 0; i <productNos.length ; i++) {
            String demo=MessageFormat.format(RedisConstant.QUOTESERVICE_PUB_QUOTE_CHANNEL,productNos[i]);
            container.addMessageListener(kLineListenerAdapter, new PatternTopic(demo));
        }
        //监听交易行情
        return container;
    }

    @Bean
    public MessageListenerAdapter marketDataListenerAdapter() {
        return new MessageListenerAdapter(redisMessageReceiver, "marketData");
    }

}

