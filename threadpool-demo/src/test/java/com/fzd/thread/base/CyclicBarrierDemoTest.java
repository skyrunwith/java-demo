package com.fzd.thread.base;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import sun.rmi.runtime.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

@Log4j2
public class CyclicBarrierDemoTest {

    @Test
    public void waitAll() throws InterruptedException {
        int n = 5;
        List<Thread> threads = new ArrayList<>();
        CyclicBarrier cyclicBarrier = new CyclicBarrier(n);
        for(int i = 0; i < n; i++){
            Thread t = new Thread(() -> {
                try {
                    log.info("before await");
                    int index = cyclicBarrier.await();
                    log.info("after await: " + index);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }, "thread " + i);
            threads.add(t);
            t.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
    }
}
