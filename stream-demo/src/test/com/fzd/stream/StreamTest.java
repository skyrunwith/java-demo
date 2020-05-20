package com.fzd.stream;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class StreamTest {

    @Test
    public void createStream(){
        List<String> strings = Arrays.asList("abc", "", "bc", "efg", "abcd", "", "jkl");
        List<String> filtered = strings.stream().filter(string -> !string.isEmpty()).collect(Collectors.toList());
        log.info(filtered);
    }
}
