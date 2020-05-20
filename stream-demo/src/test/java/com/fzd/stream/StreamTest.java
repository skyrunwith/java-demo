package com.fzd.stream;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Log4j2
public class StreamTest {

    @Test
    public void createStream(){
        List<String> strings = Arrays.asList("abc", "", "bc", "efg", "abcd", "", "jkl");
        List<String> filtered = strings.stream().filter(string -> !string.isEmpty()).collect(Collectors.toList());
        log.debug(filtered);
    }

    @Test
    public void forEach(){
        Random random = new Random();
        random.ints().limit(10).forEach(System.out::println);
    }

    @Test
    public void testMap(){
        List<Integer> numbers = Arrays.asList(3, 2, 2, 3, 7, 3, 5);
        List<Integer> squaresList = numbers.stream().map(i -> i * i).distinct().collect(Collectors.toList());
        log.info(squaresList);
    }
}
