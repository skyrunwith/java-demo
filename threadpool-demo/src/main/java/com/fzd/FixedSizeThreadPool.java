package com.fzd;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 自定义线程池
 */
public class FixedSizeThreadPool {
    //任务仓库
    private LinkedBlockingQueue<Runnable> queue;

    //线程池
    private List<Thread> workers;

    public static class Worker extends Thread {
        @Override
        public void run() {
            super.run();
        }
    }
}
