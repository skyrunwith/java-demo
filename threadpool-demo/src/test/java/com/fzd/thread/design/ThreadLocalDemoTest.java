package com.fzd.thread.design;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;
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

    @Test
    public void threadLocalMap() throws InterruptedException {
        new Thread(() -> {
            final AtomicLong nextId = new AtomicLong(0L);
            final AtomicLong nextValue = new AtomicLong(1L);
            Set<ThreadLocal> set = new HashSet<>();
            for(int i = 0; i < 9; i ++ ) {
                ThreadLocal<Long> threadLocal = ThreadLocal.withInitial(nextId::getAndIncrement);
                // set 新增
                threadLocal.set(nextValue.getAndIncrement());
                set.add(threadLocal);
            }
            //rehash resize
            ThreadLocal<Long> threadLocal = ThreadLocal.withInitial(nextId::getAndIncrement);
            threadLocal.set(nextValue.getAndIncrement());
            set.add(threadLocal);

            // set 覆盖原始值，原值为10，新值为11
            threadLocal.set(nextValue.getAndIncrement());

            //remove
            threadLocal.remove();

            log.info("over, set size: {}", set.size());
        }).start();

        Thread.sleep(1000);
    }
}
