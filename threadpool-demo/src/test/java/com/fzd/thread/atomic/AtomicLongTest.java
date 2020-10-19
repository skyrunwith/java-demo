package com.fzd.thread.atomic;

import lombok.extern.log4j.Log4j2;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 功能描述:
 *
 * @author: FZD
 * @date: 2020/10/19
 */
@Log4j2
public class AtomicLongTest {

    @Test
    public void atomicApiTest(){
        AtomicLong atomicLong = new AtomicLong();
        log.info(atomicLong.get());
        atomicLong.set(1L);
        log.info(atomicLong.get());
        atomicLong.lazySet(2L);
        log.info(atomicLong.get());
        log.info(atomicLong.getAndSet(3L));
        log.info(atomicLong.get());
        atomicLong.compareAndSet(3L, 4L);
        Assert.assertEquals(4L, atomicLong.get());
        atomicLong.weakCompareAndSet(4L, 5L);
        Assert.assertEquals(5L, atomicLong.get());
        atomicLong.getAndIncrement();
        Assert.assertEquals(6L, atomicLong.get());
        atomicLong.getAndDecrement();
        Assert.assertEquals(5L, atomicLong.get());
        Assert.assertEquals(5L, atomicLong.getAndAdd(1L));
        Assert.assertEquals(6L, atomicLong.get());
        Assert.assertEquals(7L, atomicLong.incrementAndGet());
        Assert.assertEquals(6L, atomicLong.decrementAndGet());
        Assert.assertEquals(8L, atomicLong.addAndGet(2L));
        Assert.assertEquals(8L, atomicLong.getAndUpdate(l -> l + 2L));
        Assert.assertEquals(10L, atomicLong.get());
        Assert.assertEquals(8L, atomicLong.updateAndGet(l -> l - 2L));
        Assert.assertEquals(8L, atomicLong.getAndAccumulate(92L, (left, right) -> left + right));
        Assert.assertEquals(100L, atomicLong.get());
        Assert.assertEquals(101L, atomicLong.accumulateAndGet(1L, (left, right) -> left + right));
    }

    @Test
    public void compareAndSet(){
        AtomicInteger atomicInteger = new AtomicInteger();
        boolean success = atomicInteger.compareAndSet(0, 1);
        log.info(success);
    }

    @Test
    public void atomicIncrementTest() throws InterruptedException {
        final int n = 1000000;
        final AtomicInteger atomicInteger = new AtomicInteger();
        Thread t1 = new Thread(() -> {
            int i = 0;
            while ( i++ < n){
                atomicInteger.incrementAndGet();
            }
        });
        t1.start();
        Thread t2 = new Thread(() -> {
            int i = 0;
            while ( i++ < n){
                atomicInteger.incrementAndGet();
            }
        });
        t2.start();
        t1.join();
        t2.join();
        log.info(atomicInteger.get());
    }
}
