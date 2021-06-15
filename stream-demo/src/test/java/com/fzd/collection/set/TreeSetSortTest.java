package com.fzd.collection.set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author fuzude
 * @version : 2021-06-15
 */
@Log4j2
public class TreeSetSortTest {
    private List<Book> list;
    @Before
    public void init() {
         list = Arrays.asList(
                new Book(1, "三国", 12)
                , new Book(2, "水浒", 1)
                , new Book(3, "Java++", 2)
                , new Book(1, "C++", 12)
                , new Book(2, "java 入门到放弃", 3)
                , new Book(3, "java入门到走火入魔", 6)
                , new Book(1, "helloworld", 23)
        );
    }

    /**
     * 使用forEach分组
     */
    @Test
    public void group1() {
        Map<Integer, List<Book>> groupMap = new HashMap<>();
        list.forEach(e -> {
            groupMap.computeIfAbsent(e.getId(), s -> new ArrayList<>()).add(e);
        });
        log.info(groupMap.toString());
    }

    /**
     * 使用stream.groupingBy分组
     */
    @Test
    public void group2() {
        Map<Integer, List<Book>> groupMap = list.stream().collect(Collectors.groupingBy(Book::getId));
        log.info(groupMap.toString());
    }

    /**
     * TreeSet 排序
     * BUG：TreeSet会因为排序丢失相同数据，因为TreeSet内部使用的是TreeMap，当compare返回值为0时，会判断为相同元素，则会覆盖
     */
    @Test
    public void treeSetGroupSort() {
        Map<Integer, TreeSet<Book>> groupMap = new HashMap<>();
        list.forEach(e -> {
            groupMap.computeIfAbsent(e.getId(), s -> new TreeSet<>(Comparator.comparing(Book::getNumber).reversed())).add(e);
        });
        log.info(groupMap);
    }

    @Test
    public void listGroupSort() {
        Map<Integer, List<Book>> groupMap = list.stream().sorted(Comparator.comparing(Book::getNumber).reversed()).collect(Collectors.groupingBy(Book::getId));
        log.info(groupMap);
    }

    @Test
    public void sort() {
        log.info(list.stream().sorted(Comparator.comparing(Book::getNumber).reversed()).collect(Collectors.toList()));
    }

    @Data
    @AllArgsConstructor
    class Book {
        private int id;
        private String name;
        private int number;
    }
}
