package com.baibei.shiyi.gateway;

import com.baibei.shiyi.gateway.filters.error.ErrorFilter;
import com.baibei.shiyi.gateway.filters.post.HttpLogFilter;
import com.baibei.shiyi.gateway.filters.post.ResponseWrapperFilter;
import com.baibei.shiyi.gateway.filters.pre.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/3/22 5:45 PM
 * @description: 网关启动主类
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})// 由于引用了core包导致依赖了mybatis相关的jar,所以此处需要剔除
@EnableZuulProxy
@ComponentScan("com.baibei")
public class GatewayApplication {
    public static void main(String[] args) {
        System.out.println("start execute GatewayApplication...\n");
        SpringApplication.run(GatewayApplication.class, args);
        System.out.println("end execute GatewayApplication...\n");
    }

    @Bean
    public CommonParamCheckFilter commonParamCheckFilter() {
        return new CommonParamCheckFilter();
    }

    @Bean
    public SignatureFilter signatureFilter() {
        return new SignatureFilter();
    }

    @Bean
    public AuthenticationFilter authenticationFilter() {
        return new AuthenticationFilter();
    }

    @Bean
    public CompatibleTokenFilter compatibleTokenFilter() {
        return new CompatibleTokenFilter();
    }

    @Bean
    public ErrorFilter errorFilter() {
        return new ErrorFilter();
    }

    @Bean
    public PostBodyWrapperFilter postBodyWrapperFilter() {
        return new PostBodyWrapperFilter();
    }

    @Bean
    public ResponseWrapperFilter responseWrapperFilter() {
        return new ResponseWrapperFilter();
    }

    @Bean
    public HttpLogFilter httpLogFilter() {
        return new HttpLogFilter();
    }
}
