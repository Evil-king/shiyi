package com.baibei.shiyi.trade;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/5/23 5:36 PM
 * @description:
 */
@SpringCloudApplication
@EnableTransactionManagement
@EnableFeignClients(basePackages = {"com.baibei.shiyi.product.feign","com.baibei.shiyi.user.feign","com.baibei.shiyi.account.feign",
        "com.baibei.shiyi.quotation.feign"})
@MapperScan(basePackages = {"com.baibei.*.*.dao"})
@ComponentScan("com.baibei")
@EnableHystrix
public class TradeServiceApplication {
    public static void main(String[] args) {
        System.out.println("start execute TradeServiceApplication...\n");
        SpringApplication.run(TradeServiceApplication.class, args);
        System.out.println("end execute TradeServiceApplication...\n");
    }
}
