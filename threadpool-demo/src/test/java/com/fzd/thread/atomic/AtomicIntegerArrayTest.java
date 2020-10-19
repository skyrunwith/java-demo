package com.fzd.thread.atomic;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

@Log4j2
public class AtomicIntegerArrayTest {
    @Test
    public void interArrayTest() {
        AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(10);
        log.info(atomicIntegerArray);
        for(int i =0; i < 10; i++){
            atomicIntegerArray.compareAndSet(i, 0, i);
        }
        log.info(atomicIntegerArray);
        log.info(atomicIntegerArray.getAndAdd(1, 10));
        log.info(atomicIntegerArray);
    }
}
