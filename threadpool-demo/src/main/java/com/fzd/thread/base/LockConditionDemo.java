package com.fzd.thread.base;

import lombok.extern.log4j.Log4j2;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 功能描述:
 *
 * @author: FZD
 * @date: 2020/10/15
 */
@Log4j2
public class LockConditionDemo {
    private int count = 0;
    private Lock lock = new ReentrantLock();
    public void add10K(){
        int i = 0;
        while ( i++ < 10000000){
            lock.lock();
            try {
                count += 1;
            }finally {
                lock.unlock();
            }
        }
    }

    public static void main(String... args) throws InterruptedException {
        LockConditionDemo obj = new LockConditionDemo();
        Thread t1 = new Thread(obj::add10K);
        Thread t2 = new Thread(obj::add10K);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        log.info("count: " + obj.count);
    }


}
