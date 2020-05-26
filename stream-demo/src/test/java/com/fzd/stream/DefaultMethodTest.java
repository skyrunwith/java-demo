package com.fzd.stream;

import org.junit.Test;

/**
 * 功能描述: 默认方法就是接口中有方法默认实现，而且不需要实现类去实现其方法
 *  默认方法，静态默认方法，多个默认方法
 *
 * @author: FZD
 * @date: 2020/5/26
 */
public class DefaultMethodTest {

    public interface Verhicle {
        default void print() {
            System.out.println("我是一辆车");
        }

        static void blowHonr(){
            System.out.println("按喇叭");
        }
    }

    public interface FourWheeler {
        default void print(){
            System.out.println("我是一辆四轮车");
        }
    }

    public class Car implements FourWheeler, Verhicle{
        @Override
        public void print() {
            Verhicle.super.print();
            FourWheeler.super.print();
            Verhicle.blowHonr();
            System.out.println("我是一辆四轮汽车");
        }
    }

    @Test
    public void carTest(){
        Car car = new Car();
        car.print();
    }
}
