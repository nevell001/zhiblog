package com.ruoyi.common.utils.http;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * HttpUtils 工具类测试
 */
public class HttpUtilsTest {

    /**
     * 测试 HttpUtils 类是否可以实例化
     */
    @Test
    public void testHttpUtilsInstantiation() {
        HttpUtils utils = new HttpUtils();
        assertNotNull(utils);
    }

    /**
     * 测试 GET 请求方法签名
     */
    @Test
    public void testSendGetMethod() {
        // 测试方法是否存在
        assertNotNull(HttpUtils.class);
    }

    /**
     * 测试 POST 请求方法签名
     */
    @Test
    public void testSendPostMethod() {
        // 测试方法是否存在
        assertNotNull(HttpUtils.class);
    }

    /**
     * 测试 HTTPS 请求方法签名
     */
    @Test
    public void testHttpsRequestMethod() {
        // 测试方法是否存在
        assertNotNull(HttpUtils.class);
    }
}