package com.fzd.thread.base;

public class VisibilityDemo {
    private long count = 0;

    private void add10K(){
        int idx = 0;
        while (idx++ < 1000000){
            count += 1;
        }
    }

    /**
     * 测试可见性问题
     * @return
     * @throws InterruptedException
     */
    public static long calc() throws InterruptedException {
        final VisibilityDemo visibility = new VisibilityDemo();
        //这样启动线程，两个线程之间会有时差
        Thread thread1 = new Thread(() -> visibility.add10K());
        Thread thread2 = new Thread(() -> visibility.add10K());
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        return visibility.count;
    }
}
