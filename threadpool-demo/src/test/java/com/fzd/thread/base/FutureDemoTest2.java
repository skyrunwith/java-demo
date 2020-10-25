package com.fzd.thread.base;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;

import java.util.concurrent.*;

/**
 * FutureTask 原理调试
 */
@Log4j2
public class FutureDemoTest2 {
    @Test
    public void futureTest() throws InterruptedException {
        ThreadPoolExecutor pool = new ThreadPoolExecutor(5, 10,
                1, TimeUnit.MINUTES, new LinkedBlockingQueue<>(5));
        Future<?> future = pool.submit(() -> {
            try {
                TimeUnit.MINUTES.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info(Thread.currentThread().getName());
        });
        for(int i = 0; i < 1; i++){
            Runnable runnable = () -> {
                try {
                    log.info(Thread.currentThread().getName() + "begin get");
                    future.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                log.info(Thread.currentThread().getName() + "complete get");
            };
//            pool.execute(runnable);
            Thread thread = new Thread(runnable, "thread 1");
            thread.start();
            Thread thread2 = new Thread(runnable, "thread 2");
            thread2.start();
            TimeUnit.SECONDS.sleep(1);
            thread2.interrupt();
        }
        pool.execute(() -> {
            future.cancel(true);
        });
        Thread.currentThread().join();
    }
}
