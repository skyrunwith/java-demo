package com.fzd.se8.lambda;

import com.sun.org.apache.xpath.internal.operations.And;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;

import java.util.function.BiFunction;

/**
 * 功能描述:
 *
 * @author: FZD
 * @date: 2020/10/22
 */
@Log4j2
public class lambda_07 {

    /**
     * 7. 编写静态方法 andThen
     */
    @Test
    public void andThenTest() {
        Runnable runnable = lambda_07.andThen(() -> log.info("a"), () -> log.info("b"));
        runnable.run();
    }

    public static AndThenRunnable andThen(Runnable a, Runnable b) {
        return new AndThenRunnable(a, b);
    }

    static class AndThenRunnable implements Runnable {

        private Runnable a, b;

        public AndThenRunnable(Runnable a, Runnable b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public void run() {
            a.run();
            b.run();
        }
    }
}
