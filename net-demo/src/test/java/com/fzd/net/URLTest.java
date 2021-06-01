package com.fzd.net;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * @author fuzude
 * @version : 2021-06-01
 */
@Slf4j
public class URLTest {

    @Test
    public void construct1() throws MalformedURLException, URISyntaxException {
        //通过字符串构建
        URL url1 = new URL("http://httpbin.org/get");
        log.info("字符串构建：{}", url1.toString());
        //通过base URL和filename
        URL url2 = new URL("http://httpbin.org/fzd/get");
        log.info("spec 为空: {}", new URL(url2, "").toString());
        log.info("spec 为path: {}", new URL(url2, "post").toString());
        log.info("spec 为/path: {}", new URL(url2, "/post").toString());
        log.info("spec 为schema: {}", new URL(url2, "ftp://").toString());
        log.info("spec 为schema://path: {}", new URL(url2, "ftp://xx/xx").toString());
        log.info("spec 为authority: {}", new URL(url2, "http://example.com").toString());
        //三个参数
        URL url3 = new URL("http", "httpbin.org", "/get");
        log.info(url3.toString());
        //四个参数
        URL url4 = new URL("http", "httpbin.org", 80, "/get");
        log.info(url4.toString());
        //URI
        URI uri = new URI("http", "example.com", "/hello world", "");
        log.info("uri: {}", uri.toURL().toString());
    }

    @Test
    public void parsingUrl() throws MalformedURLException {
        URL url = new URL("http://example.com:80/docs/books/tutorial/index.html?name=networking#DOWNLOADING");
        log.info("protocol = {}", url.getProtocol());
        log.info("authority = {}", url.getAuthority());
        log.info("host = {}", url.getHost());
        log.info("port = {}", url.getPort());
        log.info("path = {}", url.getPath());
        log.info("query = {}", url.getQuery());
        log.info("file = {}", url.getFile());
        log.info("ref = {}", url.getRef());
    }
}
