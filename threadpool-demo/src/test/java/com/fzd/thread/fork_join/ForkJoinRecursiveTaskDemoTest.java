package com.fzd.thread.fork_join;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * ForkJoinPool
 * RecursiveTask Demo测试
 */
@Log4j2
public class ForkJoinRecursiveTaskDemoTest {

    /**
     * 单线程递归实现计算斐波那切数列
     */
    @Test
    public void generalFibonacci(){
        Integer result = fibonacci(30);
        log.info("result = {} ", result);
    }

    public int fibonacci(int n){
        if(n <= 1){
            return n;
        }
        return fibonacci(n - 1) + fibonacci(n - 2);
    }

    /**
     * 多线程递归计算斐波那契数列
     */
    @Test
    public void fibonacciTest(){
        ForkJoinPool forkJoinPool = new ForkJoinPool(4);
        // 创建分治任务
        Fibonacci fibonacci = new Fibonacci(30);
        // 启动分治任务
        Integer result = forkJoinPool.invoke(fibonacci);
        // 输出结果
        log.info("result = {}", result);
    }

    /**
     * 递归任务
     */
    class Fibonacci extends RecursiveTask<Integer>{
        private int n;

        public Fibonacci(int n) {
            this.n = n;
        }

        @Override
        protected Integer compute() {
            if(n <= 1){
                return n;
            }
            Fibonacci f1 = new Fibonacci(n - 1);
            // 创建子任务
            f1.fork();
            Fibonacci f2 = new Fibonacci(n - 2);
            return f2.compute() + f1.join();
        }
    }
}
