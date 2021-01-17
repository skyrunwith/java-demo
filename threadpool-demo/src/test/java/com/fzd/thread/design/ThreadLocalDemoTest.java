package com.fzd.thread.design;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * ThreadLocal demo 测试
 */
@Log4j2
public class ThreadLocalDemoTest {
    @Test
    public void threadId() throws InterruptedException {
        log.info(Thread.currentThread().getName() +  " " + ThreadId.get());
        log.info(Thread.currentThread().getName() +  " " + ThreadId.get());
        log.info(Thread.currentThread().getName() +  " " + ThreadId.get());
        new Thread(() -> {
            log.info(Thread.currentThread().getName() +  " " + ThreadId.get());
        }).start();
        TimeUnit.SECONDS.sleep(3);
    }

    static class ThreadId{
        static final AtomicLong nextId = new AtomicLong(0L);

        static final ThreadLocal<Long> tl = ThreadLocal.withInitial(nextId::getAndIncrement);
        static long get(){
            return tl.get();
        }
    }
}
