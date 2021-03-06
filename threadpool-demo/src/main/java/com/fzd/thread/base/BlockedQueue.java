package com.fzd.thread.base;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 功能描述: MESA模型DEMO
 *
 * @author: FZD
 * @date: 2020/10/12
 */
public class BlockedQueue<T> {
    final Lock lock = new ReentrantLock();
    /**
     * 条件变量：队列不满
     */
    final Condition notFull = lock.newCondition();
    /**
     * 条件变量：队列不空
     */
    final Condition notEmpty = lock.newCondition();

    final String[] arr = new String[1];

    //入队
    void enq(T x) {
        lock.lock();
        try {
            // 队列已满
            while (arr.length == 1) {
                notFull.await();
            }
            // 省略入队操作...
            // 入队后，通知可出队
            notEmpty.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    void deq(T x) {
        lock.lock();
        try {
            while (arr.length == 0) {
                notEmpty.await();
            }
            // 省略出队操作
            // 出队后，通知可入队
            notFull.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
