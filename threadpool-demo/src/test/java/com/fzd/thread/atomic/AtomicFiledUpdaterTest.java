package com.fzd.thread.atomic;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * 原子化对象属性更新器
 */
@Log4j2
public class AtomicFiledUpdaterTest {
    @Test
    public void intUpdater(){
        AtomicIntegerFieldUpdater<User> atoIntUpdater = AtomicIntegerFieldUpdater.newUpdater(User.class, "age");
        User fzd = new User("fzd", 25);
        log.info(atoIntUpdater.get(fzd));
        atoIntUpdater.compareAndSet(fzd, 25, 18);
        log.info(atoIntUpdater.get(fzd));
    }

    @Test
    public void referenceUpdater(){
        AtomicReferenceFieldUpdater<User, String> refUpdater = AtomicReferenceFieldUpdater.newUpdater(User.class, String.class, "name");
        User user = new User("fzd", 25);
        log.info(refUpdater.get(user));
        refUpdater.compareAndSet(user, "fzd", "dxh");
        log.info(refUpdater.get(user));
    }

    @Data
    class User{
        volatile String name;
        volatile int age;

        public User(String name, Integer age) {
            this.name = name;
            this.age = age;
        }
    }
}
