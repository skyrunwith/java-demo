package com.fzd.language.util.spliterator;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Spliterator;
import java.util.Spliterators;

/**
 * @author fuzude
 * @version @Date: 2021-09-02
 */
@Slf4j
public class HashMapSpliteratorApi {
    HashMap<Integer, String> hashMap = new HashMap<>(8);

    @BeforeEach
    public void init () {
        hashMap.put(1, "a");
        hashMap.put(2, "b");
        hashMap.put(3, "c");
        hashMap.put(4, "d");
        hashMap.put(5, "e");
        hashMap.put(6, "f");
        hashMap.put(7, "g");
        hashMap.put(8, "g");
    }

    @Test
    public void forEachRemaining() {
        Spliterator<Integer> keySpl = hashMap.keySet().spliterator();
        keySpl.forEachRemaining(k -> log.info(k.toString()));
    }

    @Test
    public void tryAdvance() {
        Spliterator<Integer> keySpl = hashMap.keySet().spliterator();
        keySpl.tryAdvance(k -> log.info("tryAdvance: {}", k));
        keySpl.tryAdvance(k -> log.info("tryAdvance: {}", k));
    }

    @Test
    public void trySplit() {
        Spliterator<Integer> keySpl = hashMap.keySet().spliterator();
        Spliterator<Integer> first = keySpl.trySplit();
        first.forEachRemaining(k -> log.info("trySplit 1: {}", k));
        Spliterator<Integer> second = keySpl.trySplit();
        second.forEachRemaining(k -> log.info("trySplit 2: {}", k));
    }
}
