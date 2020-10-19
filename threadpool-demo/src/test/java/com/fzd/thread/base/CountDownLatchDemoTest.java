package com.fzd.thread.base;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Log4j2
public class CountDownLatchDemoTest {

    /**
     * 多个子任务执行完后，再执行新任务
     * @throws InterruptedException
     */
    @Test
    public void waitAll() throws InterruptedException {
        int n = 1000;
        CountDownLatch countDownLatch = new CountDownLatch(n);
        List<Integer> list = new ArrayList<>();
        for(int i = 0; i < n; i++){
            int finalI = i;
            new Thread(() -> {
                list.add(finalI);
                countDownLatch.countDown();
            }).start();
        }
        countDownLatch.await();
        Thread t1 = new Thread(()->{
            log.info(list.size());
        });
        t1.start();
        t1.join();
    }

    /**
     * 多个任务同时同时启动
     * 多个任务同时同时启动
     * @throws InterruptedException
     */
    @Test
    public void startAll() throws InterruptedException {
        int n = 10;
        CountDownLatch startSignal = new CountDownLatch(1);
        CountDownLatch doneSignal = new CountDownLatch(n);
        for(int i = 0; i < n; i++){
            int finalI = i;
            new Thread(() -> {
                try {
                    startSignal.await();
                    // ... do something
                    log.info(finalI);
                    doneSignal.countDown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        startSignal.countDown();
        doneSignal.await();
    }
}
