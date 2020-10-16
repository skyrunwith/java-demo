package com.fzd.thread.base;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 功能描述: 读写锁Demo
 * 简单读写锁使用
 * 读写锁应用：缓存
 * 读锁升级造成死锁
 * 写锁降级Demo：参考 ReentrantReadWriteLock doc
 *
 * @author: FZD
 * @date: 2020/10/16
 */
public class ReadWriteLockDemo {

    class Cache<K, V> {
            private final Map<K, V> m = new HashMap<>();
            private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
            private final Lock rLock = rwLock.readLock(); // 读锁
            private final Lock wLock = rwLock.writeLock(); // 写锁

            // 读缓存
            V get(K key){
                rLock.lock();
                try{
                    return m.get(key);
                }finally {
                    rLock.unlock();
                }
            }

            //写缓存
            V put(K key, V v){
                wLock.lock();
                try {
                    return m.put(key, v);
                }finally {
                    wLock.unlock();
                }

            }

        // 按需加载缓存
        V get2(K key){
            V v = null;
            rLock.lock();
            try {
                v = m.get(key);
            }finally {
                rLock.unlock();
            }
            // 如果缓存在，则返回
            if(v != null){
               return v;
            }
            // 缓存不存在，查找数据库
            wLock.lock();
            try {
                //再次验证，因为其他线程可能已经查询过数据库
                v = m.get(key);
                if(v == null){
                    // 模拟从数据库获取数据
                    v = (V) new Object();
                    m.put(key, v);
                }
            //    v = m.computeIfAbsent(key, k -> (V) new Object()); // 1.8 API
            }finally {
                wLock.unlock();
            }
            return v;
        }

        // 锁升级：读锁中获取写锁，会造成死锁
        V get3(K key){
            V v = null;
            rLock.lock();
            try {
                v = m.get(key);
                if(v != null) {
                    return v;
                }
                wLock.lock();
                try {
                    // 再次验证缓存
                    // ...
                }finally {
                    wLock.unlock();
                }
            }finally {
                rLock.unlock();
            }
            return v;
        }
    }
}
