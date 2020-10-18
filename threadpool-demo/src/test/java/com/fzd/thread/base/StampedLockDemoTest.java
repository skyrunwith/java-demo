package com.fzd.thread.base;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;

import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.StampedLock;

/**
 * 功能描述：StampedLock Demo
 */
@Log4j2
public class StampedLockDemoTest {
    private final StampedLock stampedLock = new StampedLock();
    private double x;
    private double y;

    /**
     * 写锁
     */
    @Test
    public void writeLock(){
        long stamped = stampedLock.writeLock();
        try {
            log.info("stamped: " + stamped);
            //
        }finally {
//            stampedLock.unlock(stamped); //unlock 也可以使用
            stampedLock.unlockWrite(stamped);
        }
    }

    /**
     * 悲观读锁
     */
    @Test
    public void readLock(){
        long stamped = stampedLock.readLock();
        try {
            log.info("stamped: "+ stamped);
        }finally {
            stampedLock.unlockRead(stamped);
        }
    }

    /**
     * 乐观读
     */
    @Test
    public void optimisticRead(){
        long stamped = stampedLock.tryOptimisticRead();
        log.info("stamped: " + stamped);
        if(!stampedLock.validate(stamped)){
            stamped = stampedLock.readLock();
            try {
                log.info("stamped: " + stamped);
                // ...
            }finally {
                stampedLock.unlockRead(stamped);
            }
        }
    }

    /**
     * 读锁升级
     */
    @Test
    public void stampedUpgrading(){
        long stamped = stampedLock.readLock();
        log.info("stamped: " + stamped);
        try {
            while (x == 0 && y == 0){
                long ws = stampedLock.tryConvertToWriteLock(stamped);
                log.info("ws: " + ws);
                if(ws != 0){
                    stamped = ws;
                    //...赋值
                    break;
                }else {
                    // 获取写锁失败，则手动释放读锁再手动获取写锁
                    stampedLock.unlockRead(stamped);
                    stamped = stampedLock.writeLock();
                    log.info("stamped: " + stamped);
                }
            }
        }finally {
            stampedLock.unlock(stamped);
        }
    }

    /**
     *
     */
    @Test
    public void interrupt() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            long stamped = 0;
            try {
                stamped = stampedLock.writeLockInterruptibly();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("write stamped: " + stamped);
            LockSupport.park();
        });
        t1.start();
        Thread.sleep(100);
        Thread t2 = new Thread(() -> {
            log.info("read ready!");
            long stamped = 0;
            try {
                stamped = stampedLock.readLockInterruptibly();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("read stamped: " + stamped);
        });
        t2.start();
        Thread.sleep(100);
        t2.interrupt();
        t2.join();
    }
}
