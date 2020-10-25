package com.fzd.thread.base;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.*;

/**
 * CompletableFuture Demo
 * thenRun、thenAccept、thenApply、whenComplete
 */
@Log4j2
public class CompletableFutureDemoTest2 {
    final ThreadPoolExecutor pool = new ThreadPoolExecutor(4, 8, 1, TimeUnit.MINUTES
            , new LinkedBlockingQueue<>(5), new ThreadPoolExecutor.CallerRunsPolicy());

    /**
     * thenRun 语法测试，Async线程切换测试，只有thenRunAsync传了线程池参数才会切换
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void thenRun() throws ExecutionException, InterruptedException {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info(Thread.currentThread().getName() + " future done");
        });
        CompletableFuture<Void> future2 = future.thenRun(() -> {
            log.info(Thread.currentThread().getName() + " future2 done");
        });
        CompletableFuture<Void> future3 = future2.thenRunAsync(() -> {
            log.info(Thread.currentThread().getName() + " future3 done");
        });
        // 线程池中运行，其他都是在同一线程中运行
        CompletableFuture<Void> future4 = future3.thenRunAsync(() -> {
            log.info(Thread.currentThread().getName() + " future4 done");
        }, pool);
        CompletableFuture<Void> future5 = future4.thenRunAsync(() -> {
            log.info(Thread.currentThread().getName() + " future5 done");
        });
        future5.get();
        log.info("main thread got result");
    }

    @Test
    public void thenAccept() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> Thread.currentThread().getName() + " future done");
        CompletableFuture<Void> future2 = future.thenAcceptAsync(str -> {
            log.info(str);
            log.info(Thread.currentThread().getName() + " future2 done");
        });
        future2.get();
        log.info("main thread got result");
    }

    @Test
    public void thenApply() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> Thread.currentThread().getName() + " future done");
        CompletableFuture<String> future2 = future.thenApplyAsync(str -> {
            log.info(str);
            return Thread.currentThread().getName() + " future2 done";
        });
        log.info(future2.get());
    }

    @Test
    public void whenComplete() throws InterruptedException, ExecutionException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> Thread.currentThread().getName() + "future done");
        future.whenCompleteAsync((str, t) -> {
            if (t == null) {
                log.info(str);
            } else {
                t.printStackTrace();
            }
            log.info(Thread.currentThread().getName() + "complete");
        }, pool);
        TimeUnit.SECONDS.sleep(3);
        log.info(future.get());
    }

    @Test
    public void whenCompleteException() throws InterruptedException, ExecutionException {
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
            throw new RuntimeException();
//            return Thread.currentThread().getName() + "future done")
        });
        future.whenCompleteAsync((str, t) -> {
            if (t == null) {
                log.info(str);
            } else {
                log.info(t);
            }
            log.info(Thread.currentThread().getName() + " complete");
        }, pool);
        log.info(future.get());
    }
}
