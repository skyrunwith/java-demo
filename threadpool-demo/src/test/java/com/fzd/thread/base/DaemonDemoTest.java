package com.fzd.thread.base;

import lombok.extern.log4j.Log4j2;

/**
 * 功能描述: 守护线程与用户线程测试
 *  1. 如果子线程为用户线程(默认)，则JVM会等待用户线程运行完后结束
 *  2. 如果子线程为守护线程，则JVM会直接退出
 * @author: FZD
 * @date: 2020/11/2
 */
@Log4j2
public class DaemonDemoTest {
    public static void main(String... args){
        Thread daemonThread = new Thread(() -> {
            for(;;){
                log.info("daemon");
            }
        });
        // main 函数是用户线程
        // 线程默认情况下是用户线程，主线程结束以后，子线程也是用户线程，所以JVM不会退出
        // 手动设置为守护线程，主线程结束以后，没有了用户线程，JVM 可以退出
//        daemonThread.setDaemon(true);
        daemonThread.start();
        log.info(Thread.currentThread().getName() + " is over");
    }
}
