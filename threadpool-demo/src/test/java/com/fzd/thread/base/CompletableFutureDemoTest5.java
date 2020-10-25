package com.fzd.thread.base;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Log4j2
public class CompletableFutureDemoTest5 {
    @Test
    public void runAsyncEx() throws InterruptedException, ExecutionException {
        // t 被包装为 CompletionException
        CompletableFuture<Integer> f1 = CompletableFuture.supplyAsync(() -> 1/0)
                .whenComplete((p, t) -> {
                    log.info("p = {}, t = {}" , p ,t);
                });
        TimeUnit.SECONDS.sleep(1);
//        f1.get(); // 异常未处理抛出异常
        // t 被包装为 CompletionException
        CompletableFuture<Integer> f2 = CompletableFuture.supplyAsync(() -> 1/ 0).exceptionally(t -> {
            log.info("p = {} , t = {}" ,"single exceptionally", t);
            return null;
        });
        f2.get(); //异常已处理，不抛出异常

        CompletableFuture<Void> f3 = CompletableFuture.supplyAsync(() -> 1 / 0)
                .thenAccept(x -> {
                    throw new RuntimeException("exception 2");
                })
                .exceptionally(t -> {
                    log.info("p = {} , t = {}" ,"depend exceptionally", t);
                    return null;
                });
        f3.get(); //异常发生时，其他依赖项也会异常完成
    }


    @Test
    public void handle() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> future = new CompletableFuture<>();
        CompletableFuture<Integer> handleFuture = future
                .exceptionally(t -> 1)
                .whenComplete((str, t) -> {
                    log.info("handle future complete str={}, t= {}" , str , t);
                    int a = 1 / 0;
                }).exceptionally(t -> {
                    log.info(t);
                    return 1;
                }).whenComplete((str, t) -> {
                    log.info("str = {}, t = {}", str, t);
                });
        future.completeExceptionally(new IOException());
//        log.info(future.get());
        log.info("handle future " + handleFuture.get());
    }

    @Test
    public void whenCompleteEx() {
        CompletableFuture<String> test = new CompletableFuture<>();
        test.whenComplete((result, ex) -> {
            System.out.println("stage 2：" + result + "\t" + ex);
            int a = 7 / 0;
        }).exceptionally(ex -> {
            System.out.println("stage 3：" + ex);
            return "";
        });
        test.completeExceptionally(new IOException());

//        CompletableFuture<String> test = new CompletableFuture<>();
//        test.whenComplete((result, ex) -> System.out.println("stage 2a："+ result +"\t"+ ex));
//        test.exceptionally(ex  -> {System.out.println("stage 2b："+ ex); return"";});
//        test.completeExceptionally(new IOException());
    }
}
