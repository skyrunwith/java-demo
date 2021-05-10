package com.fzd.stream;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Description:
 * @Author: fuzude
 * @Date: 2021-04-30
 */
@Log4j2
public class StreamDocTest {

    private List<Weights> weightsList;

    @Before
    public void init(){
        weightsList = new ArrayList<>();
        weightsList.add(new Weights(Color.RED, 1));
        weightsList.add(new Weights(Color.YELLOW, 2));
        weightsList.add(new Weights(Color.RED, 3));
    }

    /**
     * sequential
     */
    @Test
    public void sequential(){
        //串行结果是有序的
        weightsList.stream()
                .filter(w -> w.getColor() == Color.RED)
                .mapToInt(Weights::getWeight)
                .forEach(log::info);
    }

    /**
     * Parallelism
     */
    @Test
    public void parallel(){
        //并行结果是无序的
        weightsList.parallelStream()
                .filter(w -> w.getColor() == Color.RED)
                .mapToInt(Weights::getWeight)
                .forEach(log::info);
    }

    @Test
    public void concurrentStream(){
        List<Integer> list = new CopyOnWriteArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(5);
        list.add(4);
        log.info(list.stream().spliterator().characteristics());
    }

    /**
     * non-inference.
     * well-behaved：一个流执行终端操作前，数据源被修改了，那这个修改操作会反映到覆盖的元素中。
     */
    @Test
    public void nonInference(){
        //well-behaved
        List<String> l = new ArrayList<>(Arrays.asList("one", "two"));
        Stream<String> s = l.stream();
        l.add("three");
        //print one,two,three, 当修改stream sources 之后，由于sources不是线程安全的，stream将会改变
        log.info(s.collect(Collectors.joining(",")));

        List<String> concurrentL = new CopyOnWriteArrayList<>(Arrays.asList("one", "two"));
        Stream<String> concurrentS = concurrentL.stream();
        concurrentL.add("three");
        //print one,two, 当修改stream sources 之后，由于sources是线程安全的，stream将不会改变
        log.info(concurrentS.collect(Collectors.joining(",")));
    }

    /**
     * behaviors parameters 如果是 stateful，那么流管道的结果是不确定的，例如反复运行以下示例：
     * 结果可能是 0,0,0,0,3,0 或 0,3,0,0,0,0 等等。
     *
     * 实际使用中需注意："避免 stateful 行为参数"，使流避免 statefulness
     */
    @Test
    public void statelessBehaviors() {
        Set<Integer> seen = Collections.synchronizedSet(new HashSet<>());
        Stream<Integer> stream = Stream.of(1, 2, 3, 3, 4, 5);
        stream.parallel().map(e -> {
            if(seen.add(e)){
                return 0;
            } else {
                return e;
            }
        }).forEach(log::info);
    }

    @Test
    public void sideEffects(){
        List<String> list = new ArrayList<>();
        Stream<String> stream = Stream.of("one", "two", "three");
        // Unnecessary use of side-effects!
        stream.filter(s -> Pattern.matches("", s))
                .forEach(list::add);
        // No side-effects!
        List<String> list2 = stream.filter(s -> Pattern.matches("", s))
                .collect(Collectors.toList());

    }

    @Data
    @AllArgsConstructor
    class Weights {
        private Color color;

        private Integer weight;
    }

    enum Color {
        RED, YELLOW
    }
}
