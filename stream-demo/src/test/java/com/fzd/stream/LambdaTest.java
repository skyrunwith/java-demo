package com.fzd.stream;

import com.sun.tools.javac.util.Convert;
import org.junit.Test;

public class LambdaTest {

    @Test
    public void lambda(){
        //类型声明
        MathOperation addition = Integer::sum;
        // 不用类型声明
        MathOperation subtraction = (a, b) -> a - b;
        // 大括号及返回值
        MathOperation multiplication = ( a, b) -> { return a * b; };
        // 不用大括号及返回值
        MathOperation division = (int a, int b) -> a / b;

        System.out.println("10 + 5 = " + this.operation(10, 5, addition));
        System.out.println("10 + 5 = " + this.operation(10, 5, subtraction));
        System.out.println("10 * 5 = " + this.operation(10, 5, multiplication));
        System.out.println("10 / 5 = " + this.operation(10, 5, division));

        GreetingService greetingService1 = message -> System.out.println("Hello" + message);

        GreetingService greetingService2 = message -> System.out.println("Hello " + message);

        greetingService1.sayMessage("Runoob");

        greetingService2.sayMessage("Google");
    }

    interface MathOperation{
        int operation(int a, int b);
    }

    interface GreetingService{
        void sayMessage(String message);
    }

    private int operation(int a, int b, MathOperation mathOperation){
        return mathOperation.operation(a, b);
    }



     String salutation = "Hello ";
    @Test
    public void lambdaScope(){
        //访问成员变量
        GreetingService greetingService1 = message ->{
            System.out.println(salutation + message);

        };
        greetingService1.sayMessage("Google");
        //访问局部变量, 具有隐式final
        final int num = 1;
        Converter<Integer, String> convert = param -> {
            System.out.println(param + num);
        };
        convert.converter(2);
    }

    interface Converter<T1, T2>{
        void converter(int param);
    }
}
