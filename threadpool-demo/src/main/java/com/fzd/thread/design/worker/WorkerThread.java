package com.fzd.thread.design.worker;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.*;

/**
 * @Description: Worker Thread 模式
 * @Author: fuzude
 * @Date: 2021-02-07
 */
@Log4j2
public class WorkerThread {

    public static void main(String[] args) throws InterruptedException {
        WorkerThread wt = new WorkerThread();
        wt.avoidDeadLock();
    }

    /**
     * worker thread模式实现 server
     * 特点：使用线程池执行任务
     * @throws IOException
     */
    public void initServerSocketChannel() throws IOException {
        ExecutorService executor = Executors.newFixedThreadPool(500);
        final ServerSocketChannel ssc = ServerSocketChannel.open().bind(new InetSocketAddress(8080));
        try {
            SocketChannel sc = ssc.accept();
            executor.execute(() -> {
                try {
                    ByteBuffer rb = ByteBuffer.allocate(1024);
                    sc.read(rb);
                    Thread.sleep(2000);
                    ByteBuffer wb = (ByteBuffer) rb.flip();
                    sc.write(wb);
                    sc.close();
                } catch (Exception e){
                    log.error(e);
                }
            });
        }finally {
            ssc.close();
        }
    }

    /**
     * 正确创建线程池：
     * 有界队列，线程命名，拒绝策略
     * @return
     */
    public ThreadPoolExecutor createExecutor(){
        return new ThreadPoolExecutor(50, 500,
                60, TimeUnit.SECONDS, new LinkedBlockingQueue<>(2000),
                r -> new Thread(r, "echo-" + r.hashCode()),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    /**
     * 以下问题将造成死锁：
     * 解决方案如下：
     * 1.将线程池线程大小调大
     * 2.使用不同的线程池执行不同的任务
     * @throws InterruptedException
     */
    public void avoidDeadLock() throws InterruptedException {
        ExecutorService exec = Executors.newFixedThreadPool(2);
        CountDownLatch l1 = new CountDownLatch(2);
        for(int i = 0; i < 2; i++){
            log.info("L1");
            exec.execute(() -> {
                CountDownLatch l2 = new CountDownLatch(2);
                for(int j = 0; j < 2; j++){
                    log.info("L2");
                    exec.execute(l2::countDown);
                }
                try {
                    l2.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    l1.countDown();
                }
            });
        }
        l1.await();
        log.info("end");
    }
}
