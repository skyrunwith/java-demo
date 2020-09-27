package com.fzd;

import org.junit.Test;

import java.io.PipedInputStream;

/**
 * 功能描述: 枚举类型Demo
 *
 * @author: FZD
 * @date: 2020/9/27
 */
public class EnumTest {

    /**
     * 测试枚举类型方法
     */
    @Test
    public void EnumMethod(){
        Pizza.PizzaStatus pizzaStatus = Pizza.PizzaStatus.ORDERED;
        System.out.println(pizzaStatus.name());
        System.out.println(pizzaStatus.getDeclaringClass());
        //用于EnumSet 或者 EnumMap
        System.out.println(pizzaStatus.ordinal());
        System.out.println(pizzaStatus.compareTo(Pizza.PizzaStatus.READY));
        System.out.println(Pizza.PizzaStatus.valueOf("ORDERED"));
    }

    /**
     * 测试基于枚举类型定义的方法
     */
    @Test
    public void customMethodUseEnum(){
        Pizza pizza = new Pizza();
        System.out.println(pizza.isDeliverable()); //false
        pizza.setStatus(Pizza.PizzaStatus.READY);
        System.out.println(pizza.isDeliverable()); //true
    }

    /**
     * 测试枚举类型 == 或 equals
     * Jvm 中只保存一个枚举常量实例，所以用 == 更具有优势，equals会引起NPE异常
     */
    @Test
    public void equalsTest(){
        Pizza pizza = new Pizza();
//        System.out.println(pizza.getStatus().equals(Pizza.PizzaStatus.DELIVERED)); //NPE 异常
        System.out.println(pizza.getStatus() == Pizza.PizzaStatus.DELIVERED); //false

        pizza.setStatus(Pizza.PizzaStatus.READY);
        System.out.println(pizza.getStatus().equals(TestColor.GREEN)); //编译能通过, 运行结果 false
//        System.out.println(pizza.getStatus() == TestColor.GREEN); //编译时异常
    }
}
