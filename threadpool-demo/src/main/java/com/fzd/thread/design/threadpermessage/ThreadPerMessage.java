package com.fzd.thread.design.threadpermessage;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @Description: Thread Per Message 模式
 * @Author: fuzude
 * @Date: 2021-01-17
 */
@Log4j2
public class ThreadPerMessage {

    public static void main(String[] args) throws IOException {
        ThreadPerMessage threadPerMessage = new ThreadPerMessage();
        threadPerMessage.serverSocketChannel();
    }

    public void serverSocketChannel() throws IOException {
        final ServerSocketChannel ssc = ServerSocketChannel.open().bind(new InetSocketAddress(8080));
        try {
            while (true) {
                //接收请求
                SocketChannel sc = ssc.accept();
                //每个请求都创建一个线程
                new Thread(() -> {
                    try {
                        //读Socket
                        ByteBuffer rb = ByteBuffer.allocate(1024);
                        sc.read(rb);
                        //模拟处理请求
                        Thread.sleep(2000);
                        ByteBuffer wb = (ByteBuffer) rb.flip();
                        //写Socket
                        sc.write(wb);
                        //关闭Socket
                        sc.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            ssc.close();
        }
    }
}
