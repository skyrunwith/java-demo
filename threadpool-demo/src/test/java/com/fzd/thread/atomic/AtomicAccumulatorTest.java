package com.fzd.thread.atomic;

import lombok.extern.log4j.Log4j2;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.DoubleAccumulator;
import java.util.concurrent.atomic.DoubleAdder;

import static org.junit.Assert.assertEquals;

/**
 * 原子化的累加器测试
 */
@Log4j2
public class AtomicAccumulatorTest {
    @Test
    public void doubleAccumulator(){
        DoubleAccumulator doubleAccumulator = new DoubleAccumulator(((left, right) -> {
            log.info("left: {}, right: {}", left, right);
            return left + right;
        }), 1.1);
        assertEquals(1.1, doubleAccumulator.get(), 0);
        doubleAccumulator.accumulate(1.1);
        assertEquals(2.2, doubleAccumulator.get(), 0);
        doubleAccumulator.reset();
        assertEquals(1.1, doubleAccumulator.get(), 0);

        doubleAccumulator.accumulate(2.2);
        assertEquals(3.3, doubleAccumulator.getThenReset(), 0.01);
        assertEquals(1.1, doubleAccumulator.get(), 0);
    }

    @Test
    public void doubleAdderTest(){
        DoubleAdder doubleAdder = new DoubleAdder();
        assertEquals(0.0, doubleAdder.sum(), 0);
        doubleAdder.add(1.2);
        assertEquals(1.2, doubleAdder.sum(), 0);
        doubleAdder.add(1.3);
        assertEquals(2.5, doubleAdder.sum(), 0);
    }
}
