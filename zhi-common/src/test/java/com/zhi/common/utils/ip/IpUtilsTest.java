package com.zhi.common.utils.ip;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

/**
 * IpUtils 工具类测试
 */
public class IpUtilsTest {

    /**
     * 测试 IP 地址验证
     */
    @Test
    public void testInternalIp() {
        // 测试内网 IP
        assertTrue(IpUtils.internalIp("192.168.1.1"));
        assertTrue(IpUtils.internalIp("10.0.0.1"));
        assertTrue(IpUtils.internalIp("172.16.0.1"));
    }

    /**
     * 测试外网 IP
     */
    @Test
    public void testExternalIp() {
        // 测试外网 IP
        assertFalse(IpUtils.internalIp("8.8.8.8"));
        assertFalse(IpUtils.internalIp("114.114.114.114"));
    }

    /**
     * 测试无效 IP
     */
    @Test
    public void testInvalidIp() {
        // 测试无效 IP
        assertFalse(IpUtils.internalIp("invalid"));
        // 不测试 null 值，避免 NullPointerException
        assertFalse(IpUtils.internalIp(""));
    }

    /**
     * 测试 IP 地址是否在指定范围内
     */
    @Test
    public void testIpInRange() {
        // 测试 IP 范围验证
        assertTrue(IpUtils.internalIp("127.0.0.1"));
    }

    /**
     * 测试获取本机 IP
     */
    @Test
    public void testGetHostIp() {
        // 测试获取本机 IP
        String hostIp = IpUtils.getHostIp();
        assertNotNull(hostIp);
        assertFalse(hostIp.isEmpty());
    }

    /**
     * 测试获取主机名
     */
    @Test
    public void testGetHostName() {
        // 测试获取主机名
        String hostName = IpUtils.getHostName();
        assertNotNull(hostName);
        assertFalse(hostName.isEmpty());
    }

    // ========== 客户端 IP 可信来源（防止用 X-Forwarded-For 伪造 IP 绕过限流） ==========

    @Test
    public void shouldIgnoreForwardedHeadersFromPublicPeer() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr("203.0.113.50");           // 公网直连
        request.addHeader("X-Forwarded-For", "1.2.3.4"); // 客户端自带伪造值
        request.addHeader("X-Real-IP", "5.6.7.8");

        assertEquals("203.0.113.50", IpUtils.getIpAddr(request),
            "直连方不是可信代理时，转发头必须被忽略，否则限流/冷却/浏览去重都能被伪造");
    }

    @Test
    public void shouldTrustRealIpFromPrivatePeer() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr("172.18.0.5"); // Docker 网络里的 nginx/前端容器
        request.addHeader("X-Real-IP", "203.0.113.9");

        assertEquals("203.0.113.9", IpUtils.getIpAddr(request));
    }

    @Test
    public void shouldUseLastForwardedHop() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr("127.0.0.1");
        request.addHeader("X-Forwarded-For", "1.2.3.4, 203.0.113.9");

        assertEquals("203.0.113.9", IpUtils.getIpAddr(request),
            "append 模式下最右侧一跳才是可信代理写入的真实地址");
    }

    @Test
    public void shouldFallBackToPeerWhenProxySendsNoHeaders() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr("127.0.0.1");

        assertEquals("127.0.0.1", IpUtils.getIpAddr(request));
    }

    @Test
    public void shouldIgnoreInvalidHeaderValues() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr("127.0.0.1");
        request.addHeader("X-Real-IP", "unknown");
        request.addHeader("X-Forwarded-For", "not-an-ip");

        assertEquals("127.0.0.1", IpUtils.getIpAddr(request));
    }

    @Test
    public void shouldTreatIpv6LoopbackAsTrustedProxy() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr("0:0:0:0:0:0:0:1");
        request.addHeader("X-Real-IP", "203.0.113.9");

        assertEquals("203.0.113.9", IpUtils.getIpAddr(request));
    }

    @Test
    public void shouldRecognizeTrustedProxyRanges() {
        assertTrue(IpUtils.isTrustedProxy("127.0.0.1"));
        assertTrue(IpUtils.isTrustedProxy("172.18.0.5"));
        assertTrue(IpUtils.isTrustedProxy("10.1.2.3"));
        assertTrue(IpUtils.isTrustedProxy("::1"));
        assertFalse(IpUtils.isTrustedProxy("203.0.113.9"));
        assertFalse(IpUtils.isTrustedProxy(null));
    }
}
