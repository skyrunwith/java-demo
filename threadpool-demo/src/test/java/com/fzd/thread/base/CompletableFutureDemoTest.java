package com.fzd.thread.base;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import sun.util.locale.provider.AvailableLanguageTags;

import java.util.concurrent.*;

import static org.junit.Assert.assertTrue;

/**
 * 功能描述:
 *
 * @author: FZD
 * @date: 2020/10/20
 */
@Log4j2
public class CompletableFutureDemoTest {

    /**
     * 主动完成任务
     * @throws InterruptedException
     * @throws ExecutionException
     */
    @Test
    public void completeApi() throws InterruptedException, ExecutionException {
        CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> {
            try {
                log.info("begin runAsync");
                TimeUnit.SECONDS.sleep(3);
                log.info("end runAsync");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "auto complete";
        });
        TimeUnit.SECONDS.sleep(2);
        assertTrue(f1.complete("manual compete"));
        log.info(f1.get());
    }

    @Test
    public void completeApi2() throws ExecutionException, InterruptedException, TimeoutException {
        final int AVAILABLE_PROCESSORS = Runtime.getRuntime().availableProcessors();
        final ThreadPoolExecutor pool = new ThreadPoolExecutor(AVAILABLE_PROCESSORS, AVAILABLE_PROCESSORS * 2
                , 1, TimeUnit.MINUTES, new LinkedBlockingQueue<>(5), new ThreadPoolExecutor.CallerRunsPolicy());
        CompletableFuture<String> future = new CompletableFuture<>();
        pool.execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info(Thread.currentThread().getName() + " set future result");
//            future.complete("hello, fzd"); //如果不手动，完成，则一直阻塞
        });
        log.info("main thread wait future result");
        log.info(future.get()); // 调用get， future 会被激活
//        log.info(future.get(1, TimeUnit.SECONDS)); // TimeoutException 异常
        log.info("main thread got future result");
    }

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
