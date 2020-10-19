package com.fzd.thread.atomic;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;

/**
 * 功能描述:
 *
 * @author: FZD
 * @date: 2020/10/19
 */
@Log4j2
public class SimulatedCASTest {

    @Test
    public void addOneTest() throws InterruptedException {
        final int n = 10000000;
        final SimulatedCAS simulatedCAS = new SimulatedCAS();
        Thread t1 = new Thread(() -> {
            int i = 0;
            while ( i++ < n){
                simulatedCAS.addOne();
            }
        });
        t1.start();
        Thread t2 = new Thread(() -> {
            int i = 0;
            while ( i++ < n){
                simulatedCAS.addOne();
            }
        });
        t2.start();
        t1.join();
        t2.join();
        log.info(simulatedCAS.getCount());
    }
}
