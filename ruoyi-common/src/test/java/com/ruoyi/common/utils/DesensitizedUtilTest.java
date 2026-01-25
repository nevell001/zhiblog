package com.ruoyi.common.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * DesensitizedUtil 脱敏工具类测试
 *
 * @author ruoyi
 */
public class DesensitizedUtilTest {

    @Test
    public void testPassword() {
        // 测试密码脱敏
        assertEquals("******", DesensitizedUtil.password("123456"));
        assertEquals("********", DesensitizedUtil.password("12345678"));
        assertEquals("*****", DesensitizedUtil.password("abcde"));
        assertEquals("", DesensitizedUtil.password(""));
        assertEquals("", DesensitizedUtil.password(null));
        assertEquals("", DesensitizedUtil.password("   "));
    }

    @Test
    public void testPasswordWithSpecialChars() {
        // 测试包含特殊字符的密码脱敏
        assertEquals("******", DesensitizedUtil.password("!@#$%^"));
        assertEquals("********", DesensitizedUtil.password("AbCd1234"));
        assertEquals("*****", DesensitizedUtil.password("1@3#5"));
    }

    @Test
    public void testCarLicenseNormal() {
        // 测试普通车牌脱敏（7位）
        assertEquals("京A1***5", DesensitizedUtil.carLicense("京A12345"));
        assertEquals("沪B1***8", DesensitizedUtil.carLicense("沪B12348"));
        assertEquals("粤C1***0", DesensitizedUtil.carLicense("粤C12340"));
    }

    @Test
    public void testCarLicenseNewEnergy() {
        // 测试新能源车牌脱敏（8位）
        assertEquals("京A1234567", DesensitizedUtil.carLicense("京A1234567"));
        assertEquals("沪B1234578", DesensitizedUtil.carLicense("沪B1234578"));
        assertEquals("粤C1234590", DesensitizedUtil.carLicense("粤C1234590"));
    }

    @Test
    public void testCarLicenseInvalid() {
        // 测试无效车牌（不处理）
        assertEquals("123", DesensitizedUtil.carLicense("123"));
        assertEquals("123456789", DesensitizedUtil.carLicense("123456789"));
        assertEquals("", DesensitizedUtil.carLicense(""));
        assertEquals("", DesensitizedUtil.carLicense(null));
        assertEquals("", DesensitizedUtil.carLicense("   "));
    }

    @Test
    public void testCarLicenseWithSpecialChars() {
        // 测试包含特殊字符的车牌脱敏
        assertEquals("京A@***6", DesensitizedUtil.carLicense("京A@#$56"));
        assertEquals("沪B!***8", DesensitizedUtil.carLicense("沪B!@#78"));
    }

    @Test
    public void testPasswordLength() {
        // 测试不同长度的密码脱敏
        assertEquals("*", DesensitizedUtil.password("1"));
        assertEquals("**", DesensitizedUtil.password("12"));
        assertEquals("***", DesensitizedUtil.password("123"));
        assertEquals("****", DesensitizedUtil.password("1234"));
        assertEquals("*****", DesensitizedUtil.password("12345"));
        assertEquals("******", DesensitizedUtil.password("123456"));
        assertEquals("*******", DesensitizedUtil.password("1234567"));
        assertEquals("********", DesensitizedUtil.password("12345678"));
        assertEquals("*********", DesensitizedUtil.password("123456789"));
        assertEquals("**********", DesensitizedUtil.password("1234567890"));
    }

    @Test
    public void testCarLicenseFormat() {
        // 测试车牌格式
        String masked = DesensitizedUtil.carLicense("京A12345");
        assertEquals(7, masked.length());
        assertEquals('京', masked.charAt(0));
        assertEquals('A', masked.charAt(1));
        assertEquals('1', masked.charAt(2));
        assertEquals('*', masked.charAt(3));
        assertEquals('*', masked.charAt(4));
        assertEquals('*', masked.charAt(5));
        assertEquals('5', masked.charAt(6));

        // 新能源车牌（8位）
        String maskedNew = DesensitizedUtil.carLicense("京A123456");
        assertEquals(8, maskedNew.length());
        assertEquals('京', maskedNew.charAt(0));
        assertEquals('A', maskedNew.charAt(1));
        assertEquals('1', maskedNew.charAt(2));
        assertEquals('*', maskedNew.charAt(3));
        assertEquals('*', maskedNew.charAt(4));
        assertEquals('*', maskedNew.charAt(5));
        assertEquals('*', maskedNew.charAt(6));
        assertEquals('6', maskedNew.charAt(7));
    }
}