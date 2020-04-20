package com.baibei.shiyi.cash;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author: Longer
 * @date: 2019/10/31 11:27 AM
 * @description:出入金服务启动类
 */
@SpringCloudApplication
@EnableTransactionManagement
@EnableFeignClients(basePackages = {"com.baibei.shiyi.account.feign",
        "com.baibei.shiyi.user.feign","com.baibei.shiyi.pingan.feign","com.baibei.shiyi.settlement.feign"})
@MapperScan(basePackages = {"com.baibei.*.*.dao"})
@ComponentScan("com.baibei")
@EnableHystrix
public class CashServiceApplication {
    public static void main(String[] args) {
        System.out.println("start execute CashServiceApplication...\n");
        SpringApplication.run(CashServiceApplication.class, args);
        System.out.println("end execute CashServiceApplication...\n");
    }
}
