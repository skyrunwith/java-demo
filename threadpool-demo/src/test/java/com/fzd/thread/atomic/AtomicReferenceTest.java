package com.fzd.thread.atomic;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicReference;

@Log4j2
public class AtomicReferenceTest {

    @Test
    public void atomicReferenceTest(){
        AtomicReference<User> atoR = new AtomicReference<>();
        log.info(atoR.get());
        User fzd = new User("fzd", 25);
        atoR.compareAndSet(null, fzd);
        log.info(atoR.get());
        User dxh = new User("dxh", 26);
        atoR.compareAndSet(fzd, dxh);
        log.info(atoR.get());
    }

    @Data
    class User{
        private String name;
        private Integer age;

        public User(String name, Integer age) {
            this.name = name;
            this.age = age;
        }
    }
}
