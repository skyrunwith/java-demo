package com.fzd.thread.design.balking;

/**
 * 单次初始化中的 Balking
 */
public class InitTest {
    boolean inited = false;

    public void init(){
        synchronized (this){
            if(inited){
                return;
            }
            // 省略业务逻辑
            //doInit();
            inited = true;
        }
    }
}
