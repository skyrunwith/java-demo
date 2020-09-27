package com.fzd.stream;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * 函数式接口有且只要一个抽象方法，可以有多个非抽象方法
 * 函数式接口可以被隐式转换为lambda
 */
public class FunctionalInterfaceTest {
    @Test
    public void predicateTest(){
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        System.out.println("输出所有数据：");
        eval(list, n -> true);

        System.out.println("输出所有偶数：");
        eval(list, n-> n %2 == 0);

        System.out.println("输出所有大于3的数字");
        eval(list, n-> n > 3);
    }

    private void eval(List<Integer> list, Predicate<Integer> predicate){
        list.forEach(a -> {
            if(predicate.test(a)){
                System.out.print(a + " ");
            }
        });
    }
}
