package com.baibei.shiyi.product;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
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
@EnableFeignClients(basePackages = {"com.baibei.shiyi.product.feign","com.baibei.shiyi.order.feign"})
@MapperScan(basePackages = {"com.baibei.*.*.dao"})
@ComponentScan("com.baibei")
@EnableHystrix
public class ProductServiceApplication {
    public static void main(String[] args) {
        System.out.println("start execute ProductServiceApplication...\n");
        SpringApplication.run(ProductServiceApplication.class, args);
        System.out.println("end execute ProductServiceApplication...\n");
    }
}
