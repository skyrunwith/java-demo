package com.fzd.thread.fork_join;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * ForkJoinPool + RecursiveTask 实现 MapReduce
 */
@Log4j2
public class ForkJoinMapReduceDemoTest {

    @Test
    public void mrTest(){
        String[] fc = {"hello world",
                "hello me",
                "hello fork",
                "hello join",
                "fork join in world"};
        ForkJoinPool pool = new ForkJoinPool(3);
        // end 的值必须与数组长度一致
        MR mr = new MR(fc, 0, fc.length);
        // 创建任务
        Map<String, Integer> result = pool.invoke(mr);
        log.info(result);
    }

    class MR extends RecursiveTask<Map<String, Integer>>{
        private String[] fc;
        private int start;
        private int end;

        public MR(String[] fc, int start, int end) {
            this.fc = fc;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Map<String, Integer> compute() {
            if(end - start == 1){
                // 最后运行该方法的是
                // f(0), f(1), f(2), f(3), f(4)
                // start, end 的值分别是 0, 1;1, 2;2, 3;3, 4,4,5
                return calc(fc[start]);
            }
            int mid = (end + start) / 2;
            MR mr1 = new MR(fc, start, mid);
            mr1.fork();
            MR mr2 = new MR(fc, mid, end);
            // 计算子任务，并返回结果
            return merge(mr2.compute(), mr1.join());
        }

        /**
         * 合并计算结果
         * @param mr1
         * @param mr2
         * @return
         */
        private Map<String, Integer> merge(Map<String, Integer> mr1, Map<String, Integer> mr2) {
            Map<String, Integer> result = new HashMap<>();
            result.putAll(mr1);
            mr2.forEach((k, v) -> {
                Integer c = result.get(k);
                if(c != null){
                    result.put(k , c + v);
                }else {
                    result.put(k, v);
                }
            });
            return result;
        }

        /**
         * 统计行单词数量
         * @param fc
         * @return
         */
        private Map<String, Integer> calc(String fc) {
            Map<String, Integer> result = new HashMap<>();

            String[] wordArr = fc.split("\\s+");
            Arrays.stream(wordArr).forEach(s -> {
                Integer c = result.get(s);
                if(c != null){
                    result.put(s, c + 1);
                }else {
                    result.put(s, 1);
                }
            });
            return result;
        }
    }
}
