package com.fzd.se8.lambda;

import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;

import java.io.File;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * 功能描述:
 *
 * @author: FZD
 * @date: 2020/10/22
 */
@Log4j2
public class JavaSe8Test {
    /**
     * Arrays.sort 排序
     */
    @Test
    public void arraySort(){
        Integer a[] = {1, 3 ,2};
        Arrays.sort(a, Comparator.comparingInt(x -> x));
        log.info(Arrays.toString(a));
    }

    // 使用 方法引用
    @Test
    public void fileList(){
        File file = new File("W:\\study\\JAVA");
        // 类实例方法引用
        File[] directories = file.listFiles(File::isDirectory);
        Arrays.stream(directories).forEach(x -> log.info(x.getName()));
    }

    /**
     * 捕获变量
     */
    @Test
    public void fileNameFilter(){
        File file = new File("W:\\study\\JAVA");
        File[] directories = file.listFiles(((dir, name) -> name.contains(".pdf")));
        Arrays.stream(directories).forEach(x -> log.info(x.getName()));
    }

    /**
     * File 数组，按照目录排序，再按照路径名排序
     */
    @Test
    public void comparator(){
        File file = new File("W:\\study\\JAVA");
        File[] files = file.listFiles(File::isDirectory);
        Arrays.sort(Objects.requireNonNull(files), Comparator.comparing(x -> x.getName().length()));
//        Arrays.sort(Objects.requireNonNull(files), Comparator.comparingInt(x -> x.getName().length()));
        Arrays.stream(files).forEach(x -> log.info(x.getName()));
    }

    /**
     * for 循环中使用 lambda
     */
    @Test
    public void forLambda(){
        String[] names = {"jack", "mary", "bob"};
        List<Runnable> runnableList = new ArrayList<>();
        for(String name : names){
            runnableList.add(() -> log.info(name));
        }
//        for(int i = 0; i < names.length; i++){
              // final i 编译错误
//            runnableList.add(() -> log.info(names[i]));
//        }
    }
}
