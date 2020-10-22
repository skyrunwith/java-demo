package com.fzd.stream;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

/**
 * 方法引用通过方法名称指向一个方法
 * 减少冗余代码
 * 方法引用使用 ::
 */
public class MethodReferenceTest {

    @Test
    public void functionalTest(){
        //构造器引用
        final Car car = Car.create(Car::new);
        List<Car> cars = Collections.singletonList(car);
        //静态方法引用，相当于把car作为参数
        cars.forEach(Car::collide);
        //任意对象调用Class:method, 第一个参数会变成执行方法的对象
        cars.forEach(Car::repair);

        final Car police = Car.create(Car::new);
        //特定对象方法引用 instance:method，相当于把car作为参数
        cars.forEach(police::follow);

    }

    static class Car{
        //Supplier是jdk1.8的接口，这里和lamda一起使用了
        public static Car create(final Supplier<Car> supplier){
            return supplier.get();
        }

        public static void collide(final Car car){
            System.out.println("Collided: " + car.toString());
        }

        public void follow(final Car car){
            System.out.println("Follow the: " + car.toString());
            this.repair();
        }

        public void repair(){
            System.out.println("Repaired: " + this.toString());
        }
    }
}
