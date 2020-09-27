package com.fzd;

/**
 * Effective Java 中推荐最佳单列模式实践
 * 优点：简洁，默认提供序列化方式，绝对防止多次实例化
 * 简洁、高效、安全
 */
public enum  EnumSingleton {
    INSTANCE;
    public static EnumSingleton getInstance(){
        return INSTANCE;
    }

    private EnumStrategy strategy = EnumStrategy.NORMAL;

    public EnumStrategy getStrategy() {
        return strategy;
    }
}
