package com.fzd.thread.base;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * jdk8 stream 与 completableFuture demo
 */
@Log4j2
public class CompletableFutureStreamDemo {
    /**
     * 执行耗时 10000
     */
    @Test
    public void forTest(){
        long start = System.currentTimeMillis();
        List<String> ipList = getIpList(10);
        List<String> result = new ArrayList<>();
        for(String ip : ipList){
            result.add(rpcCall(ip, ip));
        }
        result.stream().forEach(log::info);
        log.info("for time: {}", System.currentTimeMillis() - start);
    }

    /**
     * 执行耗时 4000
     */
    @Test
    public void StreamTest(){
        long start = System.currentTimeMillis();
        List<String> ipList = getIpList(10);
        List<CompletableFuture<String>> futures = ipList.stream().map(ip -> CompletableFuture.supplyAsync(() -> rpcCall(ip, ip)))
                .collect(Collectors.toList());
        List<String> result = futures.stream().map(CompletableFuture::join).collect(Collectors.toList());
        result.stream().forEach(log::info);
        log.info("future time: {}", System.currentTimeMillis() - start);
    }

    private List<String> getIpList(int n){
        List<String> ipList = new ArrayList<>();
        for(int i = 0; i < n; i++){
            ipList.add("192.168.0." + i);
        }
        return ipList;
    }

    private static String rpcCall(String ip, String param) {
        log.info("{} rpcCall: {}" + ip, param);
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return param;
    }
}
