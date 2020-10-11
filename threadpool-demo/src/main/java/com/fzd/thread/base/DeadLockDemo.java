package com.fzd.thread.base;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述:
 *
 * @author: FZD
 * @date: 2020/10/10
 */
public class DeadLockDemo {
    /**
     * 会造成死锁
     */
    class Account {
        private int balance;
        void transfer(Account target, int amt){
            synchronized (this){ //锁定转出账户
                synchronized (target){ //锁定转入账户
                    if(this.balance > amt){
                        this.balance -= amt;
                        target.balance += amt;
                    }
                }
            }
        }
    }

    /**
     * 破坏占有且等待条件，一次性申请所有资源
     */
    class Allocator {
        private List<Object> als = new ArrayList<>();
        synchronized boolean apply(Object from, Object to){ // 一次性申请所有资源
            if(als.contains(from) || als.contains(to)) {
                return false;
            }else {
                als.add(from);
                als.add(to);
            }
            return true;
        }
        synchronized void free(Object from, Object to){ // 释放资源
            als.remove(from);
            als.remove(to);
        }

        class Account {
            private int balance;
            // 应该为单例
            private Allocator allocator;
            void transfer(DeadLockDemo.Account target, int amt){
                while(!allocator.apply(this, target)) {} // 一次性申请转出账户和转入账户，直到成功
                try {
                    synchronized(this){
                        synchronized(target){
                            // ...
                        }
                    }
                }finally {
                    allocator.free(this, target);
                }
            }
        }
    }

    class whileDeadLock{
        /**
         * 破坏循环等待条件
         */
        class Account {
            private int id;
            private int balance;
            void transfer(Account target, int amt){
                Account left = this;
                Account right = target;
                if(this.id > target.id){
                    left = right;
                    right = this;
                }
                synchronized (left){ //锁定id小的账户
                    synchronized (right){ //锁定id大的账户
                        // ...
                    }
                }
            }
        }
    }
}
