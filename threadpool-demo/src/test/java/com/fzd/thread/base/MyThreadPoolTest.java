package com.fzd.thread.base;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * 功能描述:
 *
 * @author: FZD
 * @date: 2020/10/20
 */
@Log4j2
public class MyThreadPoolTest {
    @Test
    public void poolTest() throws InterruptedException {
        MyThreadPool pool = new MyThreadPool(5, new LinkedBlockingQueue<>(2));
        for(int i = 0; i < 5; i++) {
            int finalI = i;
            pool.execute(() -> {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("my-tread-pool-execute " + finalI);
            });
        }
        Thread.currentThread().join();
    }
}
