package com.fzd.thread.base;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * 功能描述:
 *
 * @author: FZD
 * @date: 2020/10/20
 */
@Log4j2
public class CompletableFutureDemoTest {

    /**
     * CompletableFuture 完成泡茶程序
     */
    @Test
    public void makeTea(){
        CompletableFuture<Void> f1 = CompletableFuture.runAsync(() ->{
            try {
                log.info("洗水壶");
                TimeUnit.SECONDS.sleep(1);
                log.info("烧开水");
                TimeUnit.SECONDS.sleep(15);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() ->{
            try {
                log.info("洗茶壶");
                TimeUnit.SECONDS.sleep(1);
                log.info("洗茶杯");
                TimeUnit.SECONDS.sleep(2);
                log.info("拿茶叶");
                TimeUnit.SECONDS.sleep(1);
                return "龙井";
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            return "";
        });
        CompletableFuture<String> f3 = f1.thenCombine(f2, (__, u) -> {
            log.info("拿到茶叶：" + u);
            log.info("泡茶");
            return "上茶：" + u;
        });
        log.info(f3.join());
    }

    /**
     * Completable API
     */
    @Test
    public void completableFutureAPI(){
        // runAsync
        CompletableFuture.runAsync(() ->{});
        CompletableFuture.runAsync(() ->{}, null);
        // supplyAsync
        CompletableFuture.supplyAsync(()-> 1 + 1);
        CompletableFuture.supplyAsync(()-> 1 + 1, null);
        // allOf
        CompletableFuture.allOf(CompletableFuture.runAsync(()->{}));
        // anyOf
        CompletableFuture.anyOf(CompletableFuture.runAsync(()->{}));
        // completedFuture
        CompletableFuture<String> f = CompletableFuture.completedFuture("fzd");
    }

    @Test
    public void completionStageAPI(){
        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {});
        CompletableFuture<String> f0 =
                CompletableFuture.supplyAsync(
                        () -> "Hello World")      //①
                        .thenApply(s -> s + " QQ")  //②
                        .thenApply(String::toUpperCase);//③
        System.out.println(f0.join());
    }
}
