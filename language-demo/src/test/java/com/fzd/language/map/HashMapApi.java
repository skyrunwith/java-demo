package com.fzd.language.map;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * @author fuzude
 * @version @Date: 2021-08-28
 */
@Slf4j
public class HashMapApi{

    @Test
    public void tableSizeFor() {
        for(int i = 1; i < 16; i++)
            logTableSizeFor(i);
    }

    private void logTableSizeFor(int capacity) {
        int n = capacity - 1;
        log.info("capacity = {}, n = {}", capacity, n);
        log.info("n |= n >>> 1 = {}", n |= n >>> 1);
        log.info("n |= n >>> 2 = {}", n |= n >>> 2);
        log.info("n |= n >>> 4 = {}", n |= n >>> 4);
        log.info("n |= n >>> 8 = {}", n |= n >>> 8);
        log.info("n |= n >>> 16 = {}", n |= n >>> 16);
    }

    @Test
    public void initMap() {
        /**
         * 创建空map,初始化
         * loadFactor = 0.75
         * capacity = 0, threshold = 0
         */
        HashMap<String, String> map = new HashMap<>();
        log.info(map.toString());

        /**
         * 初始化参数：初始容量，会计算出默认loadFactor,threshold
         * loadFactor = 0.75
         * capacity = 0, threshold = 8
         */
        new HashMap<>(6);

        /**
         * 指定初始容量和
         * loadFactor = 0.8
         * capacity = 0, threshold = 16
         */
        new HashMap<>(10, 0.8f);

        map.put("1", "a");
        new HashMap<>(map);
    }

    /**
     * 静态方法
     */
    @Test
    public void staticMethod() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {

        log.info("hash(\"fzd\") = {}", getInvokeMethod("hash").invoke(new HashMap<>(), 0x10000));

        //String 实现了 Comparable 接口，所以返回类名
        log.info("comparableClassFor(1) = {}", getInvokeMethod("comparableClassFor").invoke(new HashMap<>(), 1));
        //Class 未实现 Comparable 接口，所以返回null
        log.info("comparableClassFor(String.class) = {}", getInvokeMethod("comparableClassFor").invoke(new HashMap<>(), String.class));

    }

    private Method getInvokeMethod(String methodName) throws IllegalAccessException, InstantiationException, NoSuchMethodException {
        Class<HashMap> clazz = HashMap.class;
        Method method = clazz.getDeclaredMethod(methodName, Object.class);
        method.setAccessible(true);
        return method;
    }

    @Test
    public void put() {
        HashMap<Integer, String> hashMap = new HashMap<>(6);
        // put第一个元素的时候初始化桶数组, 数组容量8(大于等于初始容量的最小2的幂)，阈值6
        //初始化resize, put new node
        hashMap.put(1, "a");
        //put new node
        hashMap.put(2, "b");
        //replace node
        hashMap.put(1, "c");

        /* computeIfAbsent */
        hashMap.putIfAbsent(3, "d");
        log.info("putIfAbsent: key = 3, {}", hashMap.toString());

        hashMap.compute(4, (k, v) -> k + "e");
        log.info("putIfAbsent: key = 4, {}", hashMap.toString());

        hashMap.computeIfAbsent(5, k -> k + "f");
        log.info("computeIfAbsent: key = 5, {}", hashMap.toString());

        hashMap.computeIfPresent(6, (k, v) -> k + "f");
        log.info("computeIfPresent: key = 6, {}", hashMap.toString());


        /* putAll */
        HashMap<Integer, String> putAllMap = new HashMap<>();
        putAllMap.put(1, "1");
        putAllMap.putAll(hashMap);
        log.info("putAll: " + putAllMap.toString());
    }

    /**
     * 测试：hash冲突生成桶链表，resize时不移动、移动index，
     */
    @Test
    public void resize1() {
        HashMap<Integer, String> hashMap = new HashMap<>(8);
        //扩容时不移动位置
        hashMap.put(1, "a");
        hashMap.put(2, "b");
        hashMap.put(3, "c");
        hashMap.put(4, "d");
        hashMap.put(5, "e");
        //hash 冲突, 生成链表
        hashMap.put(11, "k");
        //扩容resize, size = 7, threshold = 6
        //扩容时会重新计算key = 11的索引，11的hash值 1011 & 1000 =!0，所以需要移动位置，其他不移动位置
        hashMap.put(6, "g");
    }

    @Test
    public void treeifyBin() {
        // 因为hashmap容量小于 MIN_TREEIFY_CAPACITY(64), 当桶链表长度大于8时，此时hashmap会优先进行扩容，
        // 所以此处将容量设置为64
        HashMap<Integer, String> hashMap = new HashMap<>(64);
        int n = 1;
        int skip = 64;
        //扩容时不移动位置
        hashMap.put(n , "a");
        hashMap.put(n +     skip, "b");
        hashMap.put(n + 2 * skip, "c");
        hashMap.put(n + 3 * skip, "d");
        hashMap.put(n + 4 * skip, "e");
        hashMap.put(n + 5 * skip, "f");
        hashMap.put(n + 6 * skip, "g");
        hashMap.put(n + 7 * skip, "h");
        //链表长度大于Treeify阈值时，会转化为红黑树, 所以会进行treeifyBin
        hashMap.put(n + 8 * skip, "i");
    }

    @Test
    public void replace() {
        HashMap<Integer, String> hashMap = new HashMap<>(3);
        hashMap.put(1, "a");
        hashMap.put(2, "b");
        hashMap.put(3, "c");
        log.info("source: {}", hashMap);

        /* replace 直接替换 */
        hashMap.replace(1, "d");
        hashMap.replace(4, "d");
        log.info("replace: {}", hashMap);

        /* replace 条件替换 oldValue 匹配上才能替换*/
        hashMap.replace(2, "b", "f");
        hashMap.replace(2, "b", "g");
        log.info("replace oldValue: {}", hashMap);

        /* replaceAll */
        hashMap.replaceAll((k, v) -> k + v);
        log.info("replaceAll: {}", hashMap);
    }

    /* 以下为 hashMap 遍历 */

}


