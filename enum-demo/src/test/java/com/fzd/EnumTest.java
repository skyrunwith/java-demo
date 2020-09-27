package com.fzd;

import org.junit.Test;

import java.io.PipedInputStream;

import static org.junit.Assert.assertTrue;

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
     * 测试基于枚举类型定义api
     */
    @Test
    public void apiMethodUseEnum(){
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

    /**
     * 枚举类型可用于switch case
     */
    @Test
    public void switchTest(){
        Pizza pizza = new Pizza();
        switch (pizza.getStatus()){
            case READY:break;
            case ORDERED:break;
            case DELIVERED:break;
            default:
        }
    }

    /**
     * 枚举类型定义属性、方法、构造方法
     */
    @Test
    public void givenPizzaOrder_whenReady_thenDelivery(){
        Pizza pizza = new Pizza();
        pizza.setStatus(Pizza.PizzaStatus.READY);
        //调用枚举方法
        assertTrue(pizza.getStatus().isReady());
        //枚举类型比较 ==
        assertTrue(pizza.isDeliverable());
        //枚举属性
        pizza.printTimeToDeliver();
    }

    @Test
    public void givenPizaOrder_whenDelivered_thenPizzaGetsDeliveredAndStatusChanges(){
        Pizza pizza = new Pizza();
        pizza.setStatus(Pizza.PizzaStatus.READY);
        pizza.deliver();
        assertTrue(pizza.getStatus() == Pizza.PizzaStatus.DELIVERED);
    }
}
