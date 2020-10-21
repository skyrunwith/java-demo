package com.fzd.stream;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Log4j2
public class LambdaTest2 {
    private String name;
    @Test
    public void scopeLambda(){
        Supplier<Integer> supplier = () -> 1 + 1;
        log.info(supplier.get());
        String love = "dxh";
        Consumer<Integer> consumer = age -> {
            name = "fzd";
            log.info(name + ": " + age + " " + love);
        };
        consumer.accept(25);
//        love = "dxh"; // 编译报错 final
        Consumer<String> consumer2 = (name) -> {
            log.info(name.length());
        };
        consumer2.accept("fzd");

        Consumer<Consumer<String>> consumer3 = cons -> {
            cons.accept("I love you");
        };
        consumer3.accept(String::length);
    }
}
