package com.fzd.thread.design.guardedsuspension;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;

public class GuardedObject<T> {
    // 受保护的对象
    private T obj;
    private final Lock lock = new ReentrantLock();
    private final Condition done = lock.newCondition();
    final int timeout = 1;
    private static final Map<Object, GuardedObject> map = new ConcurrentHashMap();

    public static <K> GuardedObject create(K id){
        GuardedObject go = new GuardedObject();
        map.put(id, go);
        return go;
    }

    public static <K, T> void fireEvent(K id, T obj){
        GuardedObject<T> go = map.remove(id);
        go.onChanged(obj);
    }

    public T get(Predicate<T> predicate){
        lock.lock();
        try {
            while (!predicate.test(obj)){
                done.await(timeout, TimeUnit.SECONDS);
            }
        }catch (InterruptedException e){
            throw new RuntimeException();
        }finally {
            lock.unlock();
        }
        return obj;
    }

    public void onChanged(T obj){
        lock.lock();
        try{
            this.obj = obj;
            done.signalAll();
        }finally {
            lock.unlock();
        }
    }
}
