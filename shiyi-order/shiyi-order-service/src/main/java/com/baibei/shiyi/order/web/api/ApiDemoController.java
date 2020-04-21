package com.baibei.shiyi.order.web.api;

import com.baibei.shiyi.common.tool.api.ApiResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/5/24 9:49 AM
 * @description:
 */
@RestController
@RequestMapping("/api/order")
public class ApiDemoController {
    private AtomicInteger atomicInteger = new AtomicInteger();


    @PostMapping("/demo")
    public ApiResult demo() {
        Random random = new Random();
        int value = random.nextInt(2000);
        System.out.println("当前是第" + atomicInteger.incrementAndGet() + "次请求，请求休眠时间为" + value);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ApiResult.success();
    }
}
