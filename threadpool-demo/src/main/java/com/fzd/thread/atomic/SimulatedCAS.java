package com.fzd.thread.atomic;

import lombok.extern.log4j.Log4j2;

/**
 * 功能描述:
 *
 * @author: FZD
 * @date: 2020/10/19
 */
@Log4j2
public class SimulatedCAS {
    private volatile long count;

    public void addOne(){
        long newValue;
//        do {
            newValue = count + 1;
            log.info(cas(count, newValue));
//            log.info(count != cas(count, newValue));
//        }while (count != cas(count, newValue)); //看不懂这行代码 1 != 0
    }

    private synchronized long cas(long expect, long newValue){
        // 获取当前值
        long curValue = count;
        // 当前值 == 期望值 比较
        if(curValue == expect){
            count = newValue;
        }
        // 返回上一个值
        return curValue;
    }


    private long aaa(long expect, long newValue){
        return expect;
    }

    public long getCount() {
        return count;
    }

    public static void main(String... args){
        SimulatedCAS simulatedCAS = new SimulatedCAS();
        simulatedCAS.addOne();
        log.info("count: " + simulatedCAS.getCount());
        log.info(1!=0);
    }
}
