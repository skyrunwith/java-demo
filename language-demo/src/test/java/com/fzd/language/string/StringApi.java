package com.fzd.language.string;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * @author fuzude
 * @version @Date: 2021-08-24
 */
@Slf4j
public class StringApi {

    /**
     * 底层访问value[index]
     */
    @Test
    public void charAt() {
        String source = "abcdeabc";
        log.info("source.charAt(2) = {}", source.charAt(2));
        log.info(" = {}", source.codePointAt(2));
        log.info(" = {}", source.codePointBefore(2));
        log.info(" = {}", source.codePointCount(1, 3));
    }

    /**
     * 底层判断 toffset 是否满足条件，然后再从toffset比较每一个字符是否相等
     */
    @Test
    public void startWith() {
        String source = "abcde12345";
        log.info("source.startsWith(\"abc\") = {}", source.startsWith("abc"));
        log.info("source.startsWith(\"123\", 4) = {}", source.startsWith("123", 5));
    }

    /**
     * 底层调用 startWith方法，toffset取值为 string.length - suffix.length
     */
    @Test
    public void endWith() {
        String source = "abcde12345";
        log.info("source.endsWith(\"45\") = {}", source.endsWith("45"));
        log.info("source.endsWith(\"23\") = {}", source.endsWith("23"));
    }

    /**
     * 底层使用正则表达式匹配
     */
    @Test
    public void matches() {
        String source = "abcde12345";
        log.info(" = {}", source.matches("^[a-z]*[0-9]*"));
        log.info(" = {}", source.matches("[0-9]*$"));
    }

    @Test
    public void replace() {
        String source = "abcDAAeaaa";
        //替换单个字符
        log.info("source.replace('a', '0') = {}", source.replace('a', '0'));
        //正则表达式 字面量 子串替换
        log.info(" = {}", source.replace("aa", "00"));
    }

    /**
     * 正则表达式替换第一个子串
     */
    @Test
    public void replaceFirst() {
        String source = "aaa";
        log.info(" = {}", source.replaceFirst("[a-z]", "b"));
        log.info(" = {}", source.replaceFirst("[0-9]", "b"));
    }

    /**
     * 正则表达式替换所有子串
     */
    @Test
    public void replaceAll() {
        String source = "aabfooaabfooabfoob";
        log.info(" = {}", source.replaceAll("a*b", "-"));
    }

    /**
     * 按 regex 切割成多个子串
     */
    @Test
    public void split() {
        String source = "boo:and:foo";
        log.info("source.split(\":\", 2) = {}", Arrays.toString(source.split(":", 2)));
        log.info("source.split(\":\", 5) = {}", Arrays.toString(source.split(":", 5)));
        log.info("source.split(\":\", -2) = {}", Arrays.toString(source.split(":", -2)));
        log.info("source.split(\"o\", 5) = {}", Arrays.toString(source.split("o", 5)));
        log.info("source.split(\"o\", -2) = {}", Arrays.toString(source.split("o", -2)));
        log.info("source.split(\"o\", 0) = {}", Arrays.toString(source.split("o", 0)));
    }

    /**
     * 剪切字符串
     */
    @Test
    public void substring() {
        log.info("\"unhappy\".substring(2) = {}", "unhappy".substring(2));
        log.info("\"Harbison\".substring(3) = {}", "Harbison".substring(3));
        log.info("\"emptiness\".substring(9) = {}", "emptiness".substring(9));

        log.info("\"hamburger\".substring(4, 8) = {}", "hamburger".substring(4, 8));
        log.info("\"smiles\".substring(1, 5) = {}", "smiles".substring(1, 5));
    }

    @Test
    public void toCharArray() {
        log.info("\"ppp\".toCharArray() = {}", "ppp".toCharArray());
    }

    @Test
    public void indexOf() {
        log.info("\"name\".indexOf(\"a\") = {}", "name".indexOf("a"));
        log.info("\"name\".indexOf(\"a\", 3) = {}", "name".indexOf("a", 3));

        log.info("\"name\".indexOf(2) = {}", "name".indexOf(97));
    }


}