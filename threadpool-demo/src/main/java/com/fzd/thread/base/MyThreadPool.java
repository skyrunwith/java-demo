package com.fzd.thread.base;

import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 功能描述: 简化的线程池，仅用来说明工作原理
 *
 * @author: FZD
 * @date: 2020/10/20
 */
@Log4j2
public class MyThreadPool {
    // 阻塞队列实现生产者消费者模式
    private LinkedBlockingQueue<Runnable> workQueue;
    // 内部工作线程
    private ArrayList<WorkThread> workThreads = new ArrayList<>();

    public MyThreadPool(int coreSize, LinkedBlockingQueue<Runnable> workQueue) {
        this.workQueue = workQueue;
        for(int i = 0; i < coreSize; i++){
            WorkThread workThread = new WorkThread();
            workThreads.add(workThread);
            workThread.start();
        }
    }

    public void execute(Runnable command) throws InterruptedException {
        workQueue.put(command);
    }

    class WorkThread extends Thread {
        @Override
        public void run() {
            while (true){
                try {
                    Runnable runnable = workQueue.take();
                    runnable.run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
