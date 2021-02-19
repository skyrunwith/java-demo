package com.fzd.thread.design.interrupt;

import lombok.extern.log4j.Log4j2;

/**
 * @Description: 两阶段终止模式终止监控操作，Thread.interrupt()发送终止指令,
 *               使用Thread.currentThread().isInterrupted()作为终止标识位响应终止指令
 * @Author: fuzude
 * @Date: 2021-02-19
 */
@Log4j2
public class Proxy_3 {
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
            while (!Thread.currentThread().isInterrupted()) {
                //采集、回传
                report();
                try {
                    // 每隔两秒采集、回传一次数据
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
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
        Proxy_3 proxy = new Proxy_3();
        proxy.start();
        Thread.sleep(5000);
        proxy.stop();
    }
}
