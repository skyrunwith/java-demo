package com.fzd.thread.base;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;

/**
 * 功能描述:
 *
 * @author: FZD
 * @date: 2020/10/20
 */
@Log4j2
public class VolatileTest {

    volatile C c = new C();
    @Test
    public void volatileObj() throws InterruptedException {
        new Thread(() -> {
            while(true) {
                if(c.x == 'A') {
                    System.out.println('A');
                    c.x = 'B';
                }else {
                    //System.out.println("aaaaaa===="+c.x);
                }
            }
        }).start();

        new Thread(() -> {
            while(true) {
                if(c.x == 'B') {
                    System.out.println('B');
                    c.x = 'C';
                }else {
                    //System.out.println("bbbbbbbb===="+c.x);
                }
            }
        }).start();

        Thread t3 = new Thread(() -> {
            while(true) {
                if(c.x == 'C') {
                    System.out.println('C');
                    c.x = 'A';
                }else {
                    //System.out.println("ccccccc===="+c.x);
                }
            }
        });
        t3.start();
        t3.join();

    }

    private B b = new B();
    @Test
    public void volatileTest2() throws InterruptedException {
        new T(b, 'A', 'B').start();
        new T(b, 'B', 'C').start();
        new T(b, 'C', 'A').start();
        Thread.currentThread().join();
    }

    class C{
        public char x = 'A';
    }


    class B{
        public char x = 'A';
    }

    class T extends Thread{

        public T(B b,char sysChar,char nextChar) {
            this.b = b;
            this.sysChar = sysChar;
            this.nextChar = nextChar;
        }

        public volatile B b; //如果此变量不用volatile修饰，则不会刷新缓存，造成不打印数据
        public char sysChar;
        public char nextChar;

        @Override
        public void run() {
            while(true) {
                if(b.x == sysChar) {
                    System.out.println(sysChar);
                    b.x = nextChar;
                    //System.out.println(b);
                }else {
                    //System.out.println("ccccccc===="+c.x);
                }
            }
        }
    }
}
