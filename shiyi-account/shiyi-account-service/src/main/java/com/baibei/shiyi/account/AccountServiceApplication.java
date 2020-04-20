package com.baibei.shiyi.account;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author: hyc
 * @date: 2019/5/23 7:29 PM
 * @description:
 */
@SpringCloudApplication
@EnableTransactionManagement
@EnableFeignClients(basePackages = {"com.baibei.shiyi.user.feign","com.baibei.shiyi.publicc.feign","com.baibei.shiyi.content.feign","com.baibei.shiyi.cash.feign","com.baibei.shiyi.trade.feign"})
@MapperScan(basePackages = {"com.baibei.*.*.dao"})
@ComponentScan("com.baibei")
@EnableHystrix
public class AccountServiceApplication {
    public static void main(String[] args) {
        System.out.println("start execute AccountServiceApplication...\n");
        SpringApplication.run(AccountServiceApplication.class, args);
        System.out.println("end execute AccountServiceApplication...\n");
    }
}