package com.fzd.thread.base;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;

import java.util.concurrent.*;

/**
 * 功能描述: completableFuture 原理
 *
 * @author: FZD
 * @date: 2020/10/23
 */
@Log4j2
public class CompletableFutureDemoTest4 {
    /**
     * 调试 runAsync
     * 无返回值、非阻塞式调用
     * 1. 创建 AsyncRun（类似于 FutureTask）对象，放入 pool 中运行
     * 2. AsyncRun.run 方法中执行任务
     * 3. 任务完成后设置 result 为 AltResult
     * 4. 检查并弹出当前 stack 中依赖当前结果的行为并执行
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void runAsync() throws ExecutionException, InterruptedException {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            log.info(Thread.currentThread().getName() + " runAsync");
        });
        future.get();
    }

    /**
     * 调试 supplyAsync
     *  1. 创建 AsyncSupply，放入 pool 中运行
     *  2. 执行 AsyncSupply.get 方法
     *  3. 完成任务将 get 的值作为参数传给 complete
     *  4. 检查并弹出当前 stack 中依赖当前结果的行为并执行
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void supplyAsync() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> Thread.currentThread().getName());
        log.info(future.get());
    }

    /**
     * 调试 手动传入线程池
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void executorTest() throws ExecutionException, InterruptedException {
        ThreadPoolExecutor pool = new ThreadPoolExecutor(5, 10, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<>(5));
        CompletableFuture<Void> future = CompletableFuture.runAsync(() ->
                log.info(Thread.currentThread().getName()), pool);
        future.get();
    }

    @Test
    public void monitorRunAsyncTest() throws ExecutionException, InterruptedException {
        CompletableFuture future = monitorRunAsync(() -> {
            log.info(Thread.currentThread().getName());
        });
        future.get();
    }

    /**
     * 模拟 runAsync 方法
     * @param runnable
     * @return
     */
    private CompletableFuture monitorRunAsync(Runnable runnable){
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors()
                , 10, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<>(5));
        CompletableFuture<Void> future = new CompletableFuture<>();
        poolExecutor.execute(() -> {
            try{
                runnable.run();
                // 手动正常完成
                future.complete(null);
            }catch (Exception e){
                // 手动异常完成
                future.completeExceptionally(e);
            }
        });
        return future;
    }
}
