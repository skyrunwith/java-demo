package com.fzd.se8.lambda;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * 功能描述:
 *
 * @author: FZD
 * @date: 2020/10/22
 */
@Log4j2
public class lambda_09{

    @Test
    public void forEachIf(){
        Collection<String> collection = Arrays.asList("bob", "jack", "mary");
    }

    interface Collection2<T> extends Collection<T> {
        default void forEachIf(Consumer<? super T> consumer, Predicate<? super T> predicate){
            for(T t : this) {
                if (predicate.test(t)) {
                    consumer.accept(t);
                }
            }
        }
    }
}
