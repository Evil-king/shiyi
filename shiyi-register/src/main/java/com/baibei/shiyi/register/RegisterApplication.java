package com.baibei.shiyi.register;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/3/19 11:42 AM
 * @description:
 */
@SpringBootApplication
@EnableEurekaServer
public class RegisterApplication {
    public static void main(String[] args) {
        System.out.println("start execute RegisterApplication...\n");
        SpringApplication.run(RegisterApplication.class, args);
        System.out.println("end execute RegisterApplication...\n");
    }
}
