package com.fzd.thread.design.producer_consumer;

import jdk.nashorn.internal.objects.annotations.Constructor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 自定义log插件
 * 写文件如果同步刷盘性能会很慢，所以对于不是很重要的数据，我们往往采用异步刷盘的方式。我曾经参与过一个项目，
 * 其中的日志组件是自己实现的，采用的就是异步刷盘方式，刷盘的时机是：
 * 1. ERROR 级别的日志需要立即刷盘；
 * 2. 数据积累到 500 条需要立即刷盘；
 * 3. 存在未刷盘数据，且 5 秒钟内未曾刷盘，需要立即刷盘。
 *
 *
 * 通过调用 info()和error() 方法写入日志，这两个方法都是创建了一个日志任务 LogMsg，并添加到阻塞队列中，
 * 调用 info()和error() 方法的线程是生产者；而真正将日志写入文件的是消费者线程，在 Logger 这个类中，
 * 我们只创建了 1 个消费者线程，在这个消费者线程中，会根据刷盘规则执行刷盘操作
 * @Author: fuzude
 * @Date: 2021-03-04
 */
@Log4j2
public class MyLogAppender {
    LinkedBlockingQueue<LogMsg> bq = new LinkedBlockingQueue<>();

    static final int batchSize = 500;

    void start() throws IOException {
        File file = File.createTempFile("foo", ".log");
        final PrintWriter printWriter = new PrintWriter(file);
        ExecutorService es = Executors.newSingleThreadExecutor();
        es.execute(() -> {
            try {
                int curI = 0;
                Long startTime = System.currentTimeMillis();
                while (true) {
                    LogMsg logMsg = bq.poll(5, TimeUnit.SECONDS);
                    if(logMsg != null){
                        printWriter.write(logMsg.getMsg());
                        curI++;
                    }
                    if(curI <= 0){
                        //无需刷盘
                        continue;
                    }
                    if (LEVEL.ERROR.equals(logMsg.getLevel())
                            || curI >= batchSize
                            || (System.currentTimeMillis() - startTime) > 5000) {
                        printWriter.flush();
                        curI = 0;
                        startTime = System.currentTimeMillis();
                    }
                }
            } catch (InterruptedException e) {
                log.error(e);
            } finally {
                printWriter.flush();
                printWriter.close();
            }
        });
    }

    void info(String msg) {
        LogMsg logMsg = new LogMsg(LEVEL.INFO, msg);
        bq.add(logMsg);
    }

    void error(String msg) {
        LogMsg logMsg = new LogMsg(LEVEL.ERROR, msg);
    }

    @Data
    @AllArgsConstructor
    class LogMsg {
        LEVEL level;
        String msg;
    }

    /**
     * @Description 日志级别
     * @Author fuzude
     * @Date 2021/3/4
     * @Param
     * @return
     **/
    enum LEVEL {
        /**
         *
         */
        INFO,
        /**
         *
         */
        ERROR
    }
}
