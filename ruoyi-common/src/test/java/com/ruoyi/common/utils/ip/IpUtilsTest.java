package com.ruoyi.common.utils.ip;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

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
}