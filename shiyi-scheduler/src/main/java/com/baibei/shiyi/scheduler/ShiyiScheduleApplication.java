package com.baibei.shiyi.scheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringCloudApplication
@ComponentScan("com.baibei")
@EnableFeignClients(basePackages = {"com.baibei.shiyi.product.feign", "com.baibei.shiyi.account.feign",
        "com.baibei.shiyi.user.feign", "com.baibei.shiyi.order.feign", "com.baibei.shiyi.cash.feign",
        "com.baibei.shiyi.trade.feign","com.baibei.shiyi.settlement.feign","com.baibei.shiyi.quotation.feign"})
public class ShiyiScheduleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShiyiScheduleApplication.class, args);
    }

}
