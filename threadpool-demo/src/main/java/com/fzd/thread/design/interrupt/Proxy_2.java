package com.fzd.thread.design.interrupt;

import lombok.extern.log4j.Log4j2;

/**
 * @Description: 两阶段终止模式终止监控操作，只发送终止指令(Thread.interrupt)
 * @Author: fuzude
 * @Date: 2021-02-19
 */
@Log4j2
public class Proxy_2 {
    boolean started = false;
    /**
     * 采集线程
     */
    Thread rptThread;

    synchronized void start(){
        //不允许同时启动多个线程
        if(started){
            return;
        }
        started = true;
        rptThread = new Thread(() -> {
            while (true) {
                //采集、回传
                report();
                try {
                    // 每隔两秒采集、回传一次数据
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        rptThread.start();
        started = false;
    }

    /**
     * 终止采集功能
     */
    synchronized void stop(){
        rptThread.interrupt();
    }

    void report(){
        log.info("report");
    }

    public static void main(String[] args) throws InterruptedException {
        Proxy_2 proxy = new Proxy_2();
        proxy.start();
        Thread.sleep(5000);
        proxy.stop();
    }
}
