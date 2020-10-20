package com.fzd.thread.base;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;

import java.util.concurrent.*;

import static org.junit.Assert.*;

/**
 * 功能描述: Future Demo
 * 三个 submit 方法测试
 *
 * @author: FZD
 * @date: 2020/10/20
 */
@Log4j2
public class FutureDemoTest {

    /**
     * 测试 Callable 返回的 Future
     * submit(Callable task)
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void callableFuture() throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        Future<Integer> future = executor.submit(() -> 1 + 2);
        assertEquals(Integer.valueOf(3), future.get());
        assertTrue(future.isDone());
        assertFalse(future.isCancelled());
    }

    /**
     * 使用线程运行 FutureTask
     * future.get()
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void threadFutureTask() throws ExecutionException, InterruptedException {
        FutureTask<Integer> task = new FutureTask<>(() -> 1 + 2);
        Thread thread = new Thread(task);
        thread.start();
        Integer result = task.get();
        assertTrue(task.isDone());
        assertFalse(task.isCancelled());
        assertEquals(Integer.valueOf(3), result);
    }

    /**
     * 使用线程池运行 FutureTask
     * submit(Runnable task)
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void poolFutureTask() throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        FutureTask<Integer> task = new FutureTask<>(() -> 1 + 2);
        Future<?> future = executor.submit(task);
        Integer result = task.get();
        assertTrue(future.isDone());
        assertNull(future.get()); // Runnable 返回值的FutureTask.get值为 Null
        assertEquals(Integer.valueOf(3), result); // FutureTask 有值
    }

    /**
     * submit(Runnable task, T result)
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void poolRunnableResult() throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        User user = new User("aaa", 25);
        Future<User> future = executor.submit(new Task(user), user);
        assertEquals("bbb", future.get().getName());
        log.info(user);
    }

    /**
     * cancel(boolean interrupt) 中断
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void futureCancel() throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        Future<?> future = executor.submit(() -> {
            while (true) {
                if (Thread.currentThread().isInterrupted()) {
                    log.info("中断");
                    break;
                }
            }
        });
        TimeUnit.SECONDS.sleep(1);
        assertTrue(future.cancel(true));
        assertTrue(future.isCancelled());
        assertTrue(future.isDone());
        TimeUnit.SECONDS.sleep(5);
    }

    class Task implements Runnable {
        private User user;

        public Task(User user) {
            this.user = user;
        }

        @Override
        public void run() {
            user.name = "bbb";
        }
    }

    @Data
    class User {
        volatile String name;
        volatile int age;

        public User(String name, Integer age) {
            this.name = name;
            this.age = age;
        }
    }

    /**
     * 使用 Thread.join 实现泡茶程序
     *
     * @throws InterruptedException
     */
    @Test
    public void threadJoinMakeTea() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            try {
                log.info("洗茶壶");
                TimeUnit.SECONDS.sleep(1);
                TimeUnit.SECONDS.sleep(1);
                log.info("洗茶杯");
                TimeUnit.SECONDS.sleep(2);
                log.info("拿茶叶");
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t1.start();
        Thread t2 = new Thread(() -> {
            try {
                log.info("洗水壶");
                TimeUnit.SECONDS.sleep(1);
                log.info("烧开水");
                TimeUnit.SECONDS.sleep(1);
                t1.join(); // 省略该步骤导致不输出拿茶叶动作
                log.info("拿到茶叶");
                log.info("泡茶");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t2.start();
        t2.join();
    }

    @Test
    public void futureTaskMakeTea() throws ExecutionException, InterruptedException {
        FutureTask<String> f1 = new FutureTask<String>(() -> {
            try {
                log.info("洗茶壶");
                TimeUnit.SECONDS.sleep(1);
                TimeUnit.SECONDS.sleep(1);
                log.info("洗茶杯");
                TimeUnit.SECONDS.sleep(3);
                log.info("拿茶叶");
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "龙井";
        });
        FutureTask<String> f2 = new FutureTask<>(() -> {
            try {
                log.info("洗水壶");
                TimeUnit.SECONDS.sleep(1);
                log.info("烧开水");
                TimeUnit.SECONDS.sleep(1);
                String teaName = "";
                teaName = f1.get(); // 省略该步骤导致不输出拿茶叶动作
                log.info("拿到茶叶 " + teaName);
                log.info("泡茶");
                return "上茶" + teaName;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "上茶" + "";
        });
        Thread t1 = new Thread(f1);
        t1.start();
        Thread t2 = new Thread(f2);
        t2.start();
        log.info(f2.get());
    }
}

