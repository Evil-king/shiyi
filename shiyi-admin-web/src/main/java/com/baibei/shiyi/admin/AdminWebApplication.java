package com.baibei.shiyi.admin;

import com.baibei.shiyi.common.core.config.MyWebMvcConfigurer;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/5/21 7:29 PM
 * @description:
 */
@SpringCloudApplication
@EnableTransactionManagement
@EnableFeignClients(basePackages = {
        "com.baibei.shiyi.content.feign",
        "com.baibei.shiyi.publicc.feign",
        "com.baibei.shiyi.product.feign",
        "com.baibei.shiyi.order.feign",
        "com.baibei.shiyi.settlement.feign",
        "com.baibei.shiyi.account.feign",
        "com.baibei.shiyi.user.feign",
        "com.baibei.shiyi.trade.feign",
        "com.baibei.shiyi.cash.feign"})
@MapperScan(basePackages = {"com.baibei.**.dao"})
@ComponentScan(value = "com.baibei",excludeFilters =@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,classes = MyWebMvcConfigurer.class))
@EnableHystrix
@EnableAsync
public class AdminWebApplication {
    public static void main(String[] args) {
        System.out.println("start execute AdminWebApplication...\n");
        SpringApplication.run(AdminWebApplication.class, args);
        System.out.println("end execute AdminWebApplication...\n");
    }
}
