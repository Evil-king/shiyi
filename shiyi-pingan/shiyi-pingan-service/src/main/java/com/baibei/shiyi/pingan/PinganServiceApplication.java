package com.baibei.shiyi.pingan;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

@EnableTransactionManagement
@MapperScan(basePackages = {"com.baibei.*.*.dao"})
@ComponentScan("com.baibei")
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableFeignClients(basePackages = {"com.baibei.shiyi.account.feign","com.baibei.shiyi.cash.feign"})
public class PinganServiceApplication {

    public static void main(String[] args) {
        System.out.println("start execute ProductServiceApplication...\n");
        SpringApplication.run(PinganServiceApplication.class, args);
        System.out.println("end execute ProductServiceApplication...\n");
    }
}
