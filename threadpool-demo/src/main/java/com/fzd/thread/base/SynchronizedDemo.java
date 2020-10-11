package com.fzd.thread.base;

/**
 * 功能描述:
 *
 * @author: FZD
 * @date: 2020/10/10
 */
public class SynchronizedDemo {
    private SafeCalc safeCalc = new SafeCalc();

    public void add10K(){
        int idx = 0;
        while (idx++ < 100000){
            safeCalc.addOne(); //此时保证了线程安全，原子性
        }
    }

    public SafeCalc getSafeCalc() {
        return safeCalc;
    }

    public class SafeCalc{
        long value = 0L;

        public long getValue() {
            return value;
        }

        public synchronized void addOne() { //线程安全, 锁定对象为this
            this.value += 1;
        }

//        public synchronized static void addOne() { //线程安全, 锁定对象为SafeCalc.class
//            int i = 0;
//            while (i++ < 100000){
//                SafeCalc.value += 1;
//            }
//        }
    }
}
