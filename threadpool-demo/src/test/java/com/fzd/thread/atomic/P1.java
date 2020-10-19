package com.fzd.thread.atomic;

import org.junit.Test;

/**
 * 功能描述:
 *
 * @author: FZD
 * @date: 2020/10/19
 */
public class P1 {
    private volatile long b = 0;

    public void set1() {
        b = 0;
    }

    public void set2() {
        b = -1;
    }

    public void check() {
        // 并发条件下，该判断会成功
        if (0 != b && -1 != b) {
            System.err.println("Error");
        }
    }

    @Test
    public void p1Test() throws InterruptedException {
        P1 p1 = new P1();
        Thread t1 = new Thread(() -> {
            while (true){
                p1.set1();
            }
        });
        t1.start();
        Thread t2 = new Thread(() -> {
            while (true){
                p1.set2();
            }
        });
        t2.start();
        Thread t3 = new Thread(() -> {
            while (true){
                p1.check();
            }
        });
        t3.start();
        Thread.currentThread().join();
    }
}
