package com.fzd.thread.base;

import com.fzd.thread.base.SynchronizedDemo;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;

/**
 * 功能描述:
 *
 * @author: FZD
 * @date: 2020/10/9
 */
@Log4j2
public class SynchronizedDemoTest {

    @Test
    public void testAddOne() throws InterruptedException {
        SynchronizedDemo synchronizedDemo = new SynchronizedDemo();
        Thread A = new Thread(() -> {
            synchronizedDemo.add10K();
        });
        Thread B = new Thread(() -> {
            synchronizedDemo.add10K();
        });
        A.start();
        B.start();
        A.join();
        B.join();
        log.info(synchronizedDemo.getSafeCalc().getValue());
    }

    /**
     * 测试 同步语句表达式 demo
     */
    @Test
    public void synchronizedExpression() {
        Object o = new Object();
        // synchronized 表达式类型必须是引用类型
        synchronized ("aaa") {
        }
        //表达式类型不是引用时，将编译错误
//        synchronized (1){
//        }
    }

    /**
     * 测试 同步语句执行过程
     */
    @Test
    public void synchronizedExecution() {
        MyLock myLock = new MyLock();
        // 1.表达式异常，直接结束同步语句
//        synchronized (myLock.getIntLock()){
//            log.info("begin block");
//            log.info("complete block");
//        }

        // 2.表达式突然抛出NPE异常
//        synchronized (myLock.getNull()){
//            log.info("begin block null");
//            log.info("complete block null");
//        }

        // 3. 同步块突然结束
//        synchronized (myLock.getNormalLock()){
//            log.info("begin block exception");
//            int division = 7 / 0;
//            log.info("complete block exception");
//        }

        // 4. 同步表达式，获取锁，执行Block，释放锁都正常
        synchronized (myLock.getNormalLock()) {
            log.info("begin block normal");
            log.info("complete block normal");
        }
    }

    @Test
    public void lockObjInvoke() throws InterruptedException {
        MyLock myLock = new MyLock();
        Thread t1 = new Thread(() -> {
            synchronized (myLock.getNormalLock()) {
                int i = 0;
                while (i++ < 1000000) {
                    myLock.addOne();
                }
            }
        });
        Thread t2 = new Thread(() -> {
            synchronized (myLock.getNormalLock()) {
                int i = 0;
                while (i++ < 1000000) {
                    myLock.addOne();
                }
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        log.info("count = {}", myLock.count);
    }

    /**
     * 单线程允许多次获得锁
     */
    @Test
    public void deadLock() {
        MyLock lock = new MyLock();
        synchronized (lock) {
            synchronized (lock) {
                log.info("made it");
            }
        }
    }

    class MyLock {
        public Integer getIntLock() {
            throw new IllegalStateException();
        }

        public Integer getNull() {
            return null;
        }

        public Integer getNormalLock() {
            return 1;
        }


        private Integer count = 0;

        public void addOne() {
            this.count++;
//            synchronized (this){
//                log.info("lock obj un-synchronized invoke");
//            }
        }
    }
}
