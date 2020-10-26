package com.fzd.thread.base;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Stage 异常处理
 * 1. 一个 Stage 发生异常，其他依赖项也会发生相同异常，即返回异常Stage
 * 2. whenComplete 不论是否异常，都会运行， 如果异常未被exceptionally处理，则会返回异常Stage
 * 3. exceptionally 只有异常时才会运行，返回默认值Stage
 * 4. handle 不论是否异常，都会运行，并且可以返回默认值Stage
 */
@Log4j2
public class CompletableFutureDemoTest5 {

    @Test(expected = ExecutionException.class)
    public void runAsyncEx() throws InterruptedException, ExecutionException {
        CompletableFuture<Integer> f1 = CompletableFuture.supplyAsync(() -> 1 + 1)
                .exceptionally(t -> 1 + 1);
        // 无异常时，exceptionally 不执行
        log.info(f1.get());

        CompletableFuture<Integer> f = CompletableFuture.supplyAsync(() -> 1 / 0);
        // 异常未处理，则抛出异常 ExecutionException
        f.get();

        CompletableFuture<Integer> f2 = CompletableFuture.supplyAsync(() -> 1/ 0)
                .exceptionally(t -> {
            // t 被包装为 CompletionException
            log.info("p = {} , t = {}" ,"single exceptionally", t);
            return null;
        });
        //异常已处理，不抛出异常
        f2.get();

        CompletableFuture<Void> f3 = CompletableFuture.supplyAsync(() -> 1 / 0)
                .thenAccept(x -> {
                    throw new RuntimeException("exception 2");
                })
                .exceptionally(t -> {
                    log.info("p = {} , t = {}" ,"depend exceptionally", t);
                    return null;
                });
        //异常发生时，其他依赖项也会异常完成，所以thenAccept不会执行
        f3.get();

        CompletableFuture<Void> f4 = CompletableFuture.supplyAsync(() -> 1 / 0)
                .exceptionally( t -> {
                    log.info("p = {} , t = {}" ," exceptionally", t);
                    return null;
                })
                .thenAccept(x -> {
                    throw new RuntimeException("exception 2");
                })
                .exceptionally(t -> {
                    log.info("p = {} , t = {}" ,"depend exceptionally", t);
                    return null;
                });
        //异常被处理，依赖项不会发生异常
        f4.get();
    }


    // whenComplete 正常执行，但是stage异常未处理, whenComplete 也会返回异常
    @Test(expected = ExecutionException.class)
    public void whenCompleteEx() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> f1 = CompletableFuture.supplyAsync(() -> 1/0)
                .whenComplete((p, t) -> {
                    log.info("p = {}, t = {}" , p ,t);
                });
        TimeUnit.SECONDS.sleep(1);
        f1.get();
    }


    // stage 和 whenComplete 同时抛出异常，只有前者的异常才能抛出
    @Test
    public void whenCompleteEx2() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> f2 = CompletableFuture.supplyAsync(() ->  1 / 0)
                .whenCompleteAsync((p, t) -> Collections.emptyList().get(0))
                .exceptionally(t -> {
                    log.info("p = {}, t = {}" , "" ,t);
                    return 0;
                });
        f2.get();
    }

    /**
     * handle 返回可替换的值
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void handle() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> future = new CompletableFuture<>();
        CompletableFuture<Integer> handleFuture = future.handle((str, t) -> {
            log.info("handle future complete str={}, t= {}" , str , t);
            return 0;
        });
        future.complete(1);
        log.info("handle future " + handleFuture.get());
    }

    /**
     * handle 处理异常，并返回可替代的值
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void handleException() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> future = new CompletableFuture<>();
        CompletableFuture<Integer> handleFuture = future.handle((str, t) -> {
                    log.info("handle future complete str={}, t= {}" , str , t);
                    return 0;
                });
        future.completeExceptionally(new IOException());
//        log.info(future.get());
        log.info("handle future " + handleFuture.get());
    }
}
