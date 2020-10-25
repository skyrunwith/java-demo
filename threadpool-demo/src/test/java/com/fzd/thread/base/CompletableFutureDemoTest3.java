package com.fzd.thread.base;

import javafx.geometry.VPos;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * CompletableFuture Demo
 * thenCompose
 */
@Log4j2
public class CompletableFutureDemoTest3 {

    /**
     * 一个CompleteFuture完成后执行另一个CompleteFuture
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void thenCompose() throws ExecutionException, InterruptedException {
        // 同一个线程中执行
//        CompletableFuture<String> str = doSomeThingOne("123").thenCompose(this::doSomeThingTwo);
        // 不同线程中执行
        CompletableFuture<String> str = doSomeThingOne("123").thenComposeAsync(this::doSomeThingTwo);
        log.info(str.get());
    }

    /**
     * 两个并行的CompleteFuture完成后，再调用BiFunction
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void thenCombine() throws ExecutionException, InterruptedException {
        CompletableFuture<String> str = doSomeThingOne("123").thenCombineAsync(doSomeThingTwo("456"),
                (x, y) -> x + y);
        log.info(str.get());
    }

    /**
     * 多个并行的 CompleteFuture 完成
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void allOf() throws ExecutionException, InterruptedException {
        // future 运行在不同的线程
        List<CompletableFuture<String>> futureList = new ArrayList<>();
        futureList.add(doSomeThingOne("1"));
        futureList.add(doSomeThingOne("2"));
        futureList.add(doSomeThingOne("3"));
        futureList.add(doSomeThingOne("4"));
        CompletableFuture<Void> result = CompletableFuture.allOf(futureList.toArray(new CompletableFuture[1]));
        // 等待所有任务完成
        log.info(result.get());
    }

    /**
     * 任一一个并行的CompletableFuture 完成
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void anyOf() throws ExecutionException, InterruptedException {
        // future 运行在不同的线程
        List<CompletableFuture<String>> futureList = new ArrayList<>();
        futureList.add(doSomeThingOne("1"));
        futureList.add(doSomeThingOne("2"));
        futureList.add(doSomeThingTwo("3"));
        CompletableFuture<Object> result = CompletableFuture.anyOf(futureList.toArray(new CompletableFuture[1]));
        // 等待其中一个任务完成
        log.info(result.get());
    }

    /**
     * 异常处理
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void exceptionally() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = new CompletableFuture<>();
        new Thread(() -> {
            try {
                if(true) {
                    throw new RuntimeException("exception test");
                }
                future.complete("future complete");
            }catch (Exception e){
                // 未添加该代码会一直阻塞
                future.completeExceptionally(e);
            }
            log.info(Thread.currentThread().getName() + " set result future");
        }, "thread-1").start();
        // 抛出异常
        //        log.info(future.get());
        // 不抛出异常，并设置默认值
        log.info(future.exceptionally(t -> "default").get());
    }

    CompletableFuture<String> doSomeThingOne(String encodeCompanyId){
        return CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String id = encodeCompanyId;
            log.info(Thread.currentThread().getName() + " doSomeThing one done");
            return id;
        });
    }

    CompletableFuture<String> doSomeThingTwo(String companyId){
        return CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String id = companyId + ":bdfus";
            log.info(Thread.currentThread().getName() + " doSomeThing two done");
            return id;
        });
    }
}
