package com.fzd.thread.base;

import com.sun.org.apache.xml.internal.utils.ObjectPool;
import lombok.extern.log4j.Log4j2;
import sun.rmi.runtime.Log;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.function.Function;

/**
 * 信号量Demo：信号量允许多个线程同时进入临界区
 */
@Log4j2
public class SemaphoreDemo {
    final ObjPool<Long, String> objectPool = new ObjPool<>(5, 2L);

    public static void main(String... args) throws InterruptedException {
        SemaphoreDemo semaphoreDemo = new SemaphoreDemo();
        CountDownLatch countDownLatch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++){
            Thread thread = new Thread(() -> {
                try {
                    semaphoreDemo.objectPool.exec(t -> {
                        log.info(Thread.currentThread().getName() + " "+ t);
                        return t.toString();
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            });
            thread.start();
        }
        countDownLatch.await();
    }

    class ObjPool<T, R> {
        final List<T> pool;
        final Semaphore semaphore;

        public ObjPool(int size, T t){
            pool = new Vector<>(size);
            for(int i = 0; i < size; i++){
                pool.add(t);
            }
            semaphore = new Semaphore(size);
        }

        R exec(Function<T,R> func) throws InterruptedException {
            T t = null;
            semaphore.acquire();
            try {
                log.info(Thread.currentThread().getName() + " 进入临界区");
                t = pool.remove(0);
                return func.apply(t);
            }finally {
                pool.add(t);
                semaphore.release();
            }
        }
    }
}
