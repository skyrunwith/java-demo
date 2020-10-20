package com.fzd.thread.base;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;

import java.util.concurrent.*;

/**
 * 功能描述:
 *
 * @author: FZD
 * @date: 2020/10/20
 */
@Log4j2
public class ThreadPoolExecutorTest {
    @Test
    public void executorTest() throws InterruptedException {
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(5,
                9, 1000, TimeUnit.SECONDS, new LinkedBlockingQueue<>(5));
        poolExecutor.execute(() -> {
            log.info("thread pool: " + Thread.currentThread().getName());
        });
        Thread.sleep(100);
    }
}
