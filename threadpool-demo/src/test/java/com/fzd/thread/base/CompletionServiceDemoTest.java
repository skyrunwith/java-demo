package com.fzd.thread.base;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 批量任务执行
 */
@Log4j2
public class CompletionServiceDemoTest {
    /**
     * 批量执行并发任务
     * @throws InterruptedException
     * @throws ExecutionException
     */
    @Test
    public void completeAllTask() throws InterruptedException, ExecutionException {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10
                , 1, TimeUnit.MINUTES, new LinkedBlockingQueue<>(5));
        CompletionService<Integer> completionService = new ExecutorCompletionService<>(executor);
        List<Callable<Integer>> callables = getCallables();
        for(Callable<Integer> c : callables){
            completionService.submit(c);
        }
        for(int i = 0; i < callables.size(); i++){
            Integer result = completionService.take().get();
            if(result != null){
                log.info(result);
            }
        }
    }

    /**
     * 获取第一个不为null的结果，忽略异常，并取消其他任务
     */
    @Test
    public void completeFirstTask(){
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10
                , 1, TimeUnit.MINUTES, new LinkedBlockingQueue<>(5));
        CompletionService<Integer> completionService = new ExecutorCompletionService<>(executor);
        List<Callable<Integer>> callables = getCallables();
        List<Future<Integer>> futures = new ArrayList<>();
        Integer result = null;
        try {
            for(Callable<Integer> c : callables){
                futures.add(completionService.submit(c));
            }
            try {
                for (int i = 0; i < callables.size(); i++) {
                    Integer r = completionService.take().get();
                    if(r != null){
                        result = r;
                        break;
                    }
                }
            }catch (Exception ignored){ }
        } finally {
            for(Future f : futures){
                f.cancel(true);
            }
        }
        if (result != null) {
            log.info(result);
        }
    }

    private List<Callable<Integer>> getCallables(){
        List<Callable<Integer>> callables = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            final int finalI = i;
            callables.add(() -> {
                TimeUnit.SECONDS.sleep(1);
                log.info(Thread.currentThread().getName() + " i = {}" , finalI);
                return finalI;
            });
        }
        return callables;
    }
}
