package com.fzd.thread.base;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * 功能描述: Java 线程阻塞状态测试
 *
 * @author: FZD
 * @date: 2020/10/15
 */
@Log4j2
public class ThreadStateIO {
    @Test
    public void testThreadStateIO(){
        Thread t = new Thread(() -> {
            byte[] bytes = new byte[1024 * 100];
            for(int i =0; i < bytes.length; i++){
                bytes[i] = 1;
            }
            log.info("data ready");
            System.out.write(bytes, 0, bytes.length);
            log.info("write data done");
        }, "Java IO Thread State");
        t.start();
        while (t.isAlive()){
            log.info("thread current state: " + t.getState());
        }
    }

    @Test
    public void testThreadStateIO2() throws InterruptedException {
        Thread t = new Thread(() -> {
           try (Scanner scanner = new Scanner(System.in)){
                String input = scanner.nextLine();
                log.info(input);
           }
        }, "输入输出线程");
        t.start();
        Thread.sleep(100);
        assertEquals(t.getState(), Thread.State.RUNNABLE);
    }

    @Test
    public void testThreadStateSocket() throws InterruptedException {
        Thread t = new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(10086)){
                while (true){
                    // 阻塞
                    serverSocket.accept();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "socket 线程");
        t.start();
        Thread.sleep(100);
        assertEquals(t.getState(), Thread.State.RUNNABLE);
    }


}

