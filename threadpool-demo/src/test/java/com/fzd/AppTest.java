package com.fzd;

import static org.junit.Assert.assertTrue;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;

/**
 * 线程测试
 * 学习路线：认识 -> 应用 -> 认知 | 是什么 -> 怎么用 -> 为什么
 *
 */
@Log4j2
public class AppTest {

    /**
     * 认识：什么是线程，怎么创建线程
     */
    @Test
    public void createThread(){
        //创建线程
        Thread thread = new Thread();
        thread.start();

        new Thread(new Runnable() {
            public void run() {
                log.debug("Runnable");
            }
        }).start();
    }

    /**
     * 运用：多线程
     */
    @Test
    public void multiThread() throws InterruptedException {
        Runnable runnable = new Runnable() {
            public void run() {
                try {
                    getString();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Long bf = System.currentTimeMillis();
        for(int i = 0; i < 6; i++){
            //单线程
//            getString();

            //多线程
            Thread thread = new Thread(runnable);
            thread.start();
            thread.join();
        }
        Long en = System.currentTimeMillis();
        log.debug("程序执行时间：" + (en - bf));
    }

    public static void getString() throws InterruptedException {
        Thread.sleep(100);
        log.debug("任务执行！");
    }
}
