package com.baibei.shiyi.publicc;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/5/21 7:29 PM
 * @description:
 */
@SpringCloudApplication
@EnableTransactionManagement
//@EnableFeignClients(basePackages = {"com.baibei.shiyi.user.feign"})
@MapperScan(basePackages = {"com.baibei.shiyi.publicc.dao"})
@ComponentScan("com.baibei")
@EnableHystrix
public class PublicServiceApplication {
    public static void main(String[] args) {
        System.out.println("start execute PublicServiceApplication...\n");
        SpringApplication.run(PublicServiceApplication.class, args);
        System.out.println("end execute PublicServiceApplication...\n");
    }
}
