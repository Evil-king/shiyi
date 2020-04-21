package com.baibei.shiyi.order;

import com.baibei.shiyi.common.tool.validator.ValidateProcessor;
import com.baibei.shiyi.common.tool.validator.ValidatorContext;
import com.baibei.shiyi.order.service.IPayChannelService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/8/5 11:35
 * @description:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = OrderServiceApplication.class)
@AutoConfigureMockMvc
@Transactional()
@Slf4j
public class LockTest {

    @Autowired
    private DistributedLock distributedLock;
    @Autowired
    private IPayChannelService channelService;
    @Autowired
    private ValidateProcessor validateProcessor;

    @Test
    public void lock() {
        CountDownLatch countDownLatch = new CountDownLatch(10);
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            executorService.submit(() -> {
                String value = UUID.randomUUID().toString();
                System.out.println(Thread.currentThread().getName() + "正在执行获取锁操作");
                boolean flag = distributedLock.getLock("mylock", value, 1);
                System.out.println(Thread.currentThread().getName() + "执行获取锁结果为" + flag);
                countDownLatch.countDown();
            });
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
    }

    private static CountDownLatch countDownLatch = new CountDownLatch(10);
    private static ExecutorService executor = Executors.newFixedThreadPool(10);
    private static int THREAD_COUNT = 10;

    @Test
    public void saveList() throws Exception {
     /*   for (int i = 0; i < THREAD_COUNT; i++) {
            executor.execute(new Runnable() {
                public void run() {
                    try {
                        // 模拟业务逻辑的耗时
                        ValidatorContext requestContext = ValidatorContext.getCurrentContext();
                        requestContext.put("name", Thread.currentThread().getName());
                        validateProxyService.validate("test");
                        // 业务处理完成之后,计数器减一
                        countDownLatch.countDown();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        // 主线程一直被阻塞,直到countDownLatch的值为0
        countDownLatch.await();
        executor.shutdown();*/

        // 模拟业务逻辑的耗时
        ValidatorContext requestContext = ValidatorContext.getCurrentContext();
        requestContext.put("listDto", Thread.currentThread().getName());

        validateProcessor.validate("test");

    }


}