package com.fzd.number;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * float 和 double 会有精度问题
 */
@Log4j2
public class FloatDoubleBigDecimalDemo {

    @Test
    public void floatDemo(){
        float a = 0.06f;
        log.info(a);
        float b = a + 0.01f;
        log.info(b); //0.07
        log.info(a + 0.001); //0.06099999865889549
    }

    @Test
    public void doubleDemo(){
        double a = 0.06;
        log.info(a);
        double b = a + 0.01;
        log.info(b); //0.06999999999999999
        log.info(a + 0.01); //0.06999999999999999
    }

    @Test
    public void bigDecimalDemo(){
        double a = 0.001;
        log.info(a);//0.001
        BigDecimal bigDecimal = new BigDecimal(a);
        log.info(bigDecimal);//0.001000000000000000020816681711721685132943093776702880859375
        bigDecimal = bigDecimal.setScale(16, RoundingMode.HALF_UP); //设置16位小数
        log.info(bigDecimal); // 0.0010000000000000
    }

}
