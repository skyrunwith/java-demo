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
        Thread A = new Thread(()->{
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
}
