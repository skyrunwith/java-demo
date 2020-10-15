package com.fzd.thread.demo;

import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 功能描述: 多线程转账
 *
 * @author: FZD
 * @date: 2020/10/14
 */
@Log4j2
public class MyTransfer {

    public static void main(String... args) {
        MyTransfer myTransfer = new MyTransfer();
        myTransfer.testTransfer();
    }

    public void testTransfer() {
        Account from = new Account(100000);
        Account to = new Account(100000);
        CountDownLatch countDownLatch = new CountDownLatch(5);
        for (int i = 1; i <= 5; i++) {
            new Thread(() -> {
                from.transfer(to, 1);
                countDownLatch.countDown();
            }).start();
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("from: " + from.balance);
        log.info("to: " + to.balance);
    }

    public class Account {
        private long balance;

        public Account(long balance) {
            this.balance = balance;
        }

        private void transfer(Account target, int amount) {
            log.info(Thread.currentThread().getName() + " 尝试获取资源");
            Allocator.getInstance().apply(this, target);
            log.info(Thread.currentThread().getName() + " 获取到资源，开始转账");
            if (this.balance > amount) {
                this.balance -= amount;
                target.balance += amount;
            }
            Allocator.getInstance().free(this, target);
        }

    }

    public static class Allocator {
        private static final Allocator INSTANCE = new Allocator();

        private List<Object> locks = new ArrayList<>();
        private Allocator(){}
        public static Allocator getInstance(){
            return INSTANCE;
        }

        public synchronized void apply(Account src, Account to){
            log.info(Thread.currentThread().getName() + " 拿到资源锁");
            while (locks.contains(src) || locks.contains(to)){
                try {
                    log.info(Thread.currentThread().getName() + " 开始等待资源");
                    this.wait();
                    log.info(Thread.currentThread().getName() + " 已被唤醒，拿到锁，继续执行");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.info(Thread.currentThread().getName() + " 队列中添加资源");
            locks.add(src);
            locks.add(to);
            log.info(Thread.currentThread().getName() + " 添加资源完成");
        }

        public synchronized void free(Account src, Account to){

            log.info(Thread.currentThread().getName() + " 转账完成，释放资源，唤醒线程");
            locks.remove(src);
            locks.remove(to);
            this.notifyAll();
        }
    }
}
