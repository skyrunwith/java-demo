package com.fzd.net;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author fuzude
 * @version @Date: 2021-07-05
 */
@Log4j2
public class URITest {


    @Test
    public void construct() throws URISyntaxException {
        //绝对URI
        URI uri = new URI("https://docs.oracle.com/javase/1.3/docs/guide/collections/designfaq.html#28");
        printUri(uri);
        //相对URI
        URI uri2 = uri.resolve("../../../demo/jfc/SwingSet2/src/SwingSet2.java");
        printUri(uri2);
    }

    /**
     * 非透明URI：Opaque
     * @throws URISyntaxException
     */
    @Test
    public void construct2() throws URISyntaxException {
        URI uri = new URI("fzd", "x x x+", "fragment ");
        printUri(uri);
        assertEquals("fzd:x%20x%20x+#fragment%20", uri.toString());

        uri = new URI("mailto", "java-net@java.sun.com", "fragment #");
        printUri(uri);
    }

    @Test
    public void construct3() throws URISyntaxException {
        //只能构建绝对路径
        //host没有userInfo
        URI uri = new URI("http", "lo", "/fzd/xx", "fragment ");
        printUri(uri);
        assertEquals("http://lo/fzd/xx#fragment%20", uri.toString());
        //userInfo 为null
        assertNull(uri.getUserInfo());
    }

    /**
     * path中含有path和query，所以?会被encode
     * @throws URISyntaxException
     */
    @Test
    public void construct4() throws URISyntaxException {
        //host有userInfo
        URI uri = new URI("http", "fzd@lo", "/fzd/xx?query=name姓名", "fragment ");
        printUri(uri);
        assertEquals("http://fzd@lo/fzd/xx%3Fquery=name姓名#fragment%20", uri.toString());
        //userInfo 为fzd
        assertEquals("fzd", uri.getUserInfo());
    }

    /**
     * path拆为path和query
     * @throws URISyntaxException
     */
    @Test
    public void construct5() throws URISyntaxException {
        URI uri = new URI("http", "fzd@lo", "/fzd/xx", "query=name姓名", "fragment ");
        printUri(uri);
        assertEquals("http://fzd@lo/fzd/xx?query=name姓名#fragment%20", uri.toString());
        //userInfo 为fzd
        assertEquals("fzd", uri.getUserInfo());
    }

    @Test
    public void construct6() throws URISyntaxException {
        URI uri = new URI("http", "fzd", "lo", -1, "/fzd/xx", "query=name姓名", "fragment ");
        printUri(uri);
        assertEquals("http://fzd@lo/fzd/xx?query=name姓名#fragment%20", uri.toString());
    }

    @Test
    public void normalize() throws URISyntaxException {
        URI uri = new URI("././../demo/jfc/SwingSet2/src/SwingSet2.java");
        log.info("uri = {}", uri.toString());
        // 1.remove .
        log.info("normalize uri = {}\n", uri.normalize().toString());

        // 2.remove /dsd..
        uri = new URI("/dsd/../demo/jfc/SwingSet2/src/SwingSet2.java");
        log.info("uri = {}", uri.toString());
        log.info("normalize uri = {}", uri.normalize().toString());

        // 3.a:b/c/d path为relative，并且第一部分包含:, 则解析为opaque URI，schema为a
        uri = new URI("a:b/c/d");
        log.info("uri = {}", uri.toString());
        log.info("normalize uri = {}", uri.normalize().toString());
        printUri(uri);
    }

    @Test
    public void parseServerAuthority() throws URISyntaxException {
        URI uri = new URI("http", "foo:bar", "/fzd/xx", "query=name姓名", "fragment ");
        printUri(uri);
        try {
            // foo:bar是合理的registry-based authority，但不是合法的server-based authority，所以此处会抛出异常URISyntaxException
            printUri(uri.parseServerAuthority());
        } catch (Exception e) {
            log.error(e);
        }
    }

    /**
     * 返回 given uri 自身
     * @throws URISyntaxException
     */
    @Test
    public void relativizeReturnGivenUri() throws URISyntaxException {
        URI thisUri = new URI("http://foo@bar/fzd/user/one?query=name#fragment");

        // given uri is opaque
        URI givenOpaqueUri = thisUri.relativize(URI.create("mailto:java-net@java.sun.com#opaque"));
        log.info("givenOpaqueUri = {}", givenOpaqueUri.toString());

        // scheme and authority 不同
        URI schemeNotIdentical = thisUri.relativize(URI.create("ftp://foo@bar/fzd/user/one?query=name#fragment"));
        log.info("schemeNotIdentical = {}", schemeNotIdentical.toString());
        URI authorityNotIdentical = thisUri.relativize(URI.create("http://bar@foo/fzd/user/one?query=name#fragment"));
        log.info("authorityNotIdentical = {}", authorityNotIdentical.toString());

        //this uri path is not prefix of the path of the given uri,this uri path 不是given uri path对前缀
        URI notPrefixPath = thisUri.relativize(URI.create("http://foo@bar/fzd/user/two?query=name#fragment"));
        log.info("notPrefixPath = {}", notPrefixPath.toString());
    }

    /**
     * 计算相对URI，会移除thisURI对path部分
     * @throws URISyntaxException
     */
    @Test
    public void relativizeByCompute() throws URISyntaxException {
        URI thisUri = new URI("http://foo@bar/fzd/user/one?query=name#fragment");
        URI compute = thisUri.relativize(URI.create("http://foo@bar/fzd/user/one/compute?query=name#fragment"));
        log.info("notPrefixPath = {}", compute.toString());
        printUri(compute);
    }

    /**
     * 解析并返回given uri
     */
    @Test
    public void resolveReturnGivenUri() throws URISyntaxException {
        URI thisUri = new URI("http://foo@bar/fzd/user/one?query=name#fragment");
        // given uri 是绝对路径，或者是opaque
        log.info("givenUriAbsolute = {}", thisUri.resolve("http://foo@bar/fzd/user/two?query=name#fragment"));
        log.info("givenUriOpaque = {}", thisUri.resolve("mailto:java-net@java.sun.com#opaque"));
    }

    /**
     * 根据this uri，计算出新uri
     */
    @Test
    public void resolveByCompute() throws URISyntaxException {
        URI thisUri = new URI("http://foo@bar/fzd/user/one?query=name#fragment");
        // 只有fragment部分
        assertEquals("http://foo@bar/fzd/user/one?query=name#xxx", thisUri.resolve("#xxx").toString());
        // authority 以given uri为准
        assertEquals("http://bar@foo/path", thisUri.resolve("//bar@foo/path").toString());
        // given uri是相对uri，
        assertEquals("http://foo@bar/fzd/user/new/path", thisUri.resolve("new/path").toString());
        assertEquals("http://foo@bar/fzd/new/path", thisUri.resolve("../new/path").toString());
        assertEquals("http://foo@bar/new/path", thisUri.resolve("/new/path").toString());
    }


    private void printUri(URI uri) {
        log.info("** URI = {} **", uri.toString());
        log.info("** ASCIIString = {} **", uri.toASCIIString());
        log.info("Scheme = {}", uri.getScheme());
        log.info("SchemeSpecificPart = {}", uri.getSchemeSpecificPart());
        log.info("Authority = {}", uri.getAuthority());
        log.info("UserInfo = {}", uri.getUserInfo());
        log.info("Host = {}", uri.getHost());
        log.info("Port = {}", uri.getPort());
        log.info("Path = {}", uri.getPath());
        log.info("Query = {}", uri.getQuery());
        log.info("Fragment = {}", uri.getFragment());
        log.info("** -- **");

        log.info("isAbsolute = {}", uri.isAbsolute());
        log.info("isOpaque = {}", uri.isOpaque());
        log.info("** -- **");

        log.info("RawSchemeSpecificPart = {}", uri.getRawSchemeSpecificPart());
        log.info("RawAuthority = {}", uri.getRawAuthority());
        log.info("RawUserInfo = {}", uri.getRawUserInfo());
        log.info("RawPath = {}", uri.getRawPath());
        log.info("RawQuery = {}", uri.getRawQuery());
        log.info("RawFragment  = {}", uri.getRawFragment());

        log.info("** Print URI End **");
    }
}
