package com.fzd.thread.design.balking;

/**
 * Double-checking 单例中的 Balking模式
 */
public class Singleton {
    private static volatile Singleton singleton;

    public static Singleton getInstance(){
        if(singleton == null) {
            synchronized (Singleton.class) {
                if (singleton == null) {
                    singleton = new Singleton();
                }
            }
        }
        return singleton;
    }
}
