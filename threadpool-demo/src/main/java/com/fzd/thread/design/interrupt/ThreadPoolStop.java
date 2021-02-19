package com.fzd.thread.design.interrupt;

import java.util.List;
import java.util.concurrent.*;

/**
 * @Description: 优雅的终止线程池
 * @Author: fuzude
 * @Date: 2021-02-19
 */
public class ThreadPoolStop {
    ExecutorService pool = Executors.newFixedThreadPool(5);

    void shutdown(){
        pool.shutdown();
    }

    void shutdownNow() throws InterruptedException {
        List<Runnable> list = pool.shutdownNow();
        pool.awaitTermination(1, TimeUnit.MINUTES);
    }

    public static void main(String[] args) throws InterruptedException {
        ThreadPoolStop stop = new ThreadPoolStop();
        stop.shutdown();
        stop.shutdownNow();
    }
}
