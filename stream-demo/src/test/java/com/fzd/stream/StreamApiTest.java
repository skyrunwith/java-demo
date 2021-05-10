package com.fzd.stream;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
public class StreamApiTest {

    @Test
    public void iterator(){
        Iterator<Integer> iterator = Stream.of(1, 2, 3, 5, 4).iterator();
        while (iterator.hasNext()){
            log.info(iterator.next());
        }
    }

    @Test
    public void spliterator(){
        Spliterator<Integer> spliterator = Stream.of(1, 2, 3, 5, 4).spliterator();
        spliterator.forEachRemaining(log::info);
    }

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

    @Test
    public void filter(){
        List<String> strings = Arrays.asList("abc", "", "bc", "efg", "abcd", "", "jkl");
        long count = strings.stream().filter(string -> !string.isEmpty()).count();
        log.debug(count);
    }

    @Test
    public void limit(){
        Random random = new Random();
        random.ints().limit(10).forEach(log::debug);
    }

    @Test
    public void sorted(){
        Random random = new Random();
        random.ints().limit(10).sorted().forEach(log::debug);

        List<String> strings = Arrays.asList("abc", "", "bc", "efg", "abcd", "", "jkl");
        strings.stream().sorted(Comparator.comparingInt(String::length)).forEach(log::debug);
    }

    @Test
    public void parallel(){
        List<String> strings = Arrays.asList("abc", "", "bc", "efg", "abcd", "", "jkl");
        long count = strings.parallelStream().filter(string -> !string.isEmpty()).count();
        log.debug(count);
    }

    @Test
    public void collectors(){
        List<String> strings = Arrays.asList("abc", "", "bc", "efg", "abcd", "", "jkl");
        List<String> filtered = strings.stream().filter(string -> !string.isEmpty()).collect(Collectors.toList());
        log.debug(filtered);
        String mergeString = filtered.stream().collect(Collectors.joining(","));
        log.debug(mergeString);
    }

    @Test
    public void flatMap(){
        //使用flatmap
        List<List<String>> strings = List.of(List.of("apple", "banana"), List.of("meat", "hamburg"));
        String flatMapResult = strings.stream().flatMap(Collection::stream).collect(Collectors.joining(","));
        log.info(flatMapResult);
        // 使用map
        String mapResult = strings.stream().map(list -> String.join(",", list)).collect(Collectors.joining(","));
        log.info(mapResult);
    }

    @Test
    public void peek(){
        String result = Stream.of("one", "two", "three", "four")
                .filter(e -> e.length() > 3)
                .peek(log::info)
                .map(String::toUpperCase)
                .peek(log::info)
                .collect(Collectors.joining(","));
        log.info(result);
    }

    @Test
    public void skip(){
        Stream.of(1, 2, 3, 4).skip(2).forEach(log::info);
    }

    @Test
    public void order(){
        Stream.of(1, 3, 4, 5 ,2).forEachOrdered(log::info);
        log.info(Stream.of(1, 3, 4, 5 ,2).isParallel());
    }

    @Test
    public void toArray(){
        //toArray
        log.info(Arrays.stream(Stream.of(1, 3, 4, 5 ,2).toArray()).map(Object::toString).collect(Collectors.joining(",")));
        //toArray(IntFunction<A[]> generator)
        log.info(Arrays.stream(Stream.of(1, 3, 5 ,4).toArray(i -> { log.info(i); return List.of(2, 2, 2, 2).toArray();})).map(Object::toString).collect(Collectors.joining(",")));
    }
}
