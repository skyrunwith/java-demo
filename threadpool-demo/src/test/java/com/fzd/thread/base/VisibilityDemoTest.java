package com.fzd.thread.base;

import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;

@Log4j2
public class VisibilityDemoTest {
    @Test
    public void testVisibility() throws InterruptedException {
        long count = VisibilityDemo.calc();
        log.info(count);
    }
}
