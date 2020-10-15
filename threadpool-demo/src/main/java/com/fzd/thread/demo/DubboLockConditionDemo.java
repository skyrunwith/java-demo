package com.fzd.thread.demo;

import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 功能描述: Dubbo 异步转同步原理
 *
 * @author: FZD
 * @date: 2020/10/15
 */
public class DubboLockConditionDemo {
    private final Lock lock = new ReentrantLock();
    private final Condition done = lock.newCondition();
    private Object reps = null;

    /**
     * 通过该方法等待结果
     * @param timeout
     * @return
     * @throws InterruptedException
     * @throws TimeoutException
     */
    public Object get(int timeout) throws InterruptedException, TimeoutException {
        long start = System.nanoTime();
        lock.lock();
        try {
            while (!isDone()){
                done.awaitNanos(timeout);
                long cur = System.nanoTime();
                if(isDone() || cur - start > timeout){
                    break;
                }
            }
        }finally {
            lock.unlock();
        }
        // 如果reps == null，则表示超时退出
        if(!isDone()){
            throw new TimeoutException();
        }
        return reps;
    }

    /**
     * RPC 结果是否已返回
     * @return
     */
    boolean isDone(){
        return reps != null;
    }

    /**
     *  RPC 结果调用方法
     * @param reps
     */
    void doReceived(Object reps){
        lock.lock();
        try{
            this.reps = reps;
            done.signal();
        }finally {
            lock.unlock();
        }
    }
}
