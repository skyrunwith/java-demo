package com.fzd.expression;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;

/**
 * 表达式测试
 */
@Log4j2
public class ExpressionTest {
    /**
     * && || 表达式短路测试
     */
    @Test
    public void _ifTest(){
        int a = 0, b = 1;
        if(a++ == 1 && b++ ==2){
            log.info("a={}, b={}", a, b);
        }else {
            // a++ ==1 为false短路，所以 b++ 不执行
            log.info("a={}, b={}", a, b);
        }
        if(a++ == 1 || b++ == 2){
            // a++ == false 短路，b ++ 不执行
            log.info("a={}, b={}", a, b);
        }else {
            log.info("a={}, b={}", a, b);
        }
    }

    @Test
    public void ifTest(){
        int a = 0;
        int count = 0;
        for(;;){
            count ++;
            if(a == 0){
                a += 1;
            }else if(a == 1){
                a += 1;
            }else if( a== 2){
                log.info("a = {} , count = {}", a, count);
                break;
            }
        }
    }

}
