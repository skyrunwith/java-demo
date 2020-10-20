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
        CompletableFuture<String> f3 = f1.thenCombine(f2, (t, u) -> {
            log.info(t);
            log.info("拿到茶叶：" + u);
            log.info("泡茶");
            return "上茶：" + u;
        });
        log.info(f3.join());
    }
}
