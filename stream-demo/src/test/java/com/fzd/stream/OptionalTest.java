package com.fzd.stream;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Optional;

@Log4j2
public class OptionalTest {

    @Test
    public void testOptional(){
        Integer value1 = null;
        Integer value2 = 10;
        Optional<Integer> a = Optional.ofNullable(value1);
        Optional<Integer> b = Optional.of(value2);
        log.debug(sum(a, b));
    }

    private Integer sum(Optional<Integer> a, Optional<Integer> b){
        log.debug("第一个值存在：" + a.isPresent());
        log.debug("第二个值存在：" + b.isPresent());

        Integer value1 = a.orElse(0);
        Integer value2 = b.get();
        return value1 + value2;
    }
}
