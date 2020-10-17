package com.fzd.thread.base;

import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

/**
 *  synchronized 实现objectPool，比较与 SemaphoreDemo 区别
 */
@Log4j2
public class SynchronizedSemaphoreDemo {
    final ObjPool<Long, String> objectPool = new ObjPool<>(4, 2L);

    public static void main(String... args) throws InterruptedException {
        SynchronizedSemaphoreDemo semaphoreDemo = new SynchronizedSemaphoreDemo();
        CountDownLatch countDownLatch = new CountDownLatch(5);
        for (int i = 0; i < 5; i++){
            Thread thread = new Thread(() -> {
                try {
                    semaphoreDemo.objectPool.exec(t -> {
                        log.info(Thread.currentThread().getName() + " "+ t);
                        return t.toString();
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    countDownLatch.countDown();
                }
            });
            thread.start();
        }
        countDownLatch.await();
    }

    class ObjPool<T, R> {
        final List<T> pool;
        final Lock lock = new ReentrantLock();

        public ObjPool(int size, T t){
            pool = new Vector<>(size);
            for(int i = 0; i < size; i++){
                pool.add(t);
            }
        }

        R exec(Function<T,R> func) throws InterruptedException {
            T t = null;
            lock.lock();
            try {
                log.info(Thread.currentThread().getName() + " 进入临界区");
                t = pool.remove(0);
                return func.apply(t);
            }finally {
                lock.unlock();
            }
        }
    }
}
