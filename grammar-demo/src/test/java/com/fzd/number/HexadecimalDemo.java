package com.fzd.number;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;

/**
 * 十六进制测试
 */
@Log4j2
public class HexadecimalDemo {

    /**
     * 打印 -1， 1 的 二进制数
     */
    @Test
    public void negativeNumber(){
        log.info(Integer.toBinaryString(-1));
        log.info(Integer.toBinaryString(1));
    }
}
