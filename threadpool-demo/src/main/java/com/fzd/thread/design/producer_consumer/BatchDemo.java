package com.fzd.thread.design.producer_consumer;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Description: 生产者模式批量任务
 * 创建了 5 个消费者线程负责批量执行 SQL，这 5 个消费者线程以 while(true){} 循环方式批量地获取任务并批量地执行。
 * 需要注意的是，从任务队列中获取批量任务的方法 pollTasks() 中，首先是以阻塞方式获取任务队列中的一条任务，
 * 而后则是以非阻塞的方式获取任务；之所以首先采用阻塞方式，是因为如果任务队列中没有任务，这样的方式能够避免无谓的循环。
 * @Author: fuzude
 * @Date: 2021-03-04
 */
@Log4j2
public class BatchDemo {
    LinkedBlockingQueue<Task> bq = new LinkedBlockingQueue<>(2000);

    void start() {
        ExecutorService es = Executors.newFixedThreadPool(5);
        es.execute(() -> {
            try {
                while (true) {
                    //获取批量任务
                    List<Task> tasks = pollTasks();
                    //执行批量任务
                    executeTasks(tasks);
                }
            }catch (InterruptedException e){
                log.error(e);
            }
        });
    }

    List<Task> pollTasks() throws InterruptedException {
        List<Task> tasks = new ArrayList<>();
        // 阻塞式获取一条任务
        Task task = bq.take();
        //task为null，则跳出循环
        while (task != null) {
            tasks.add(task);
            //非阻塞式获取任务
            task = bq.poll();
        }
        return tasks;
    }

    void executeTasks(List<Task> tasks) {
        //忽略批量执行逻辑
    }

    @Data
    class Task {
        private String taskId;
    }
}
