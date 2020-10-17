package com.fzd.thread.base;

import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static org.junit.Assert.assertEquals;

/**
 * 功能描述:
 *
 * @author: FZD
 * @date: 2020/10/16
 */
@Log4j2
public class ReadWriteLockDemoTest {

    @Test
    public void get(){
        ReadWriteLockDemo readWriteLockDemo = new ReadWriteLockDemo();
        ReadWriteLockDemo.Cache<String, Long> cache = readWriteLockDemo.new Cache<>();
        cache.put("fzd", 1L);
        assertEquals(1L, cache.get("fzd").longValue());
    }

    /**
     * 造成死锁：读锁中获取写锁
     */
    @Test
    public void upgrading(){
        ReadWriteLockDemo readWriteLockDemo = new ReadWriteLockDemo();
        ReadWriteLockDemo.Cache<String, Long> cache = readWriteLockDemo.new Cache<>();
        cache.upgrading("fzd");
    }

    /**
     * 正常运行：写锁中获取读锁
     */
    @Test
    public void downGrading(){
        ReadWriteLockDemo readWriteLockDemo = new ReadWriteLockDemo();
        ReadWriteLockDemo.Cache<String, Long> cache = readWriteLockDemo.new Cache<>();
        cache.downGrading("fzd");
     }
}
